package session.controllers;

//error redirect itself
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import session.DTO.AccountDto;
import session.DTO.createAccountDTO;
import session.responseHandler.BodyResponseWithTime;
import session.responseHandler.Exception.ServerException;
import session.service.AccountService;
import session.service.OtpService;
import session.utils.State;
import session.utils.Status;
import java.util.Objects;

/**
 * AccountController
 */

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService service;
    private final OtpService otpService;
    public AccountController(AccountService service, OtpService otpService) {
        this.service = service;
        this.otpService = otpService;
    }
//admin view all account
    @RequestMapping("/get")
    public ResponseEntity<Object> getAll() {
        try {
            //View All account base on AccountDto format
            return new ResponseEntity<>(service.getAllAccount(), new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            throw new ServerException(e.getMessage(), new HttpHeaders());
        }
    }
    @RequestMapping("/login")
    public ResponseEntity<Object> loginTest(HttpSession http, @RequestBody createAccountDTO accountDto) {

        State<AccountDto> res = service.login(accountDto.getUsername(), accountDto.getPassword());
        switch (res.getStatus()) {
            case NOT_FOUND -> {
                return new ResponseEntity<>(new BodyResponseWithTime<>("Username Not Found", HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
            }
            case UNAUTHORIZED -> {
                return new ResponseEntity<>(new BodyResponseWithTime<>("Wrong Password", HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
            }
        }
        //Store id in session
        http.setAttribute("user", res.getData().id());
        return new ResponseEntity<>(new BodyResponseWithTime<>("Welcome " + res.getData().username(), HttpStatus.OK.value()), HttpStatus.OK);
    }
    @RequestMapping("/getInformation")
    public ResponseEntity<Object> findUserById(HttpSession session) {
        try {
            int id = (int) session.getAttribute("user");
            AccountDto res = service.findUser(id);
            return new ResponseEntity<>(new BodyResponseWithTime<>(res, HttpStatus.OK.value()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new BodyResponseWithTime<>("Please login to continue", HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
        }
    }
    //Create Account
    @PostMapping("/register")
    public ResponseEntity<Object> create(@RequestBody createAccountDTO accountDto) {
        HttpHeaders headers = new HttpHeaders();
        try {
            State<AccountDto> res = service.createAccount(accountDto);
            switch (res.getStatus()) {
                case EXIST_USERNAME -> {
                    return new ResponseEntity<>(new BodyResponseWithTime<>(res.getData().username() + " already exists", HttpStatus.BAD_GATEWAY.value()), headers, HttpStatus.BAD_GATEWAY);
                }
                case EXIST_EMAIL -> {
                    return new ResponseEntity<>(new BodyResponseWithTime<>(res.getData().email() + " exist", HttpStatus.BAD_GATEWAY.value()), headers, HttpStatus.BAD_GATEWAY);
                }
            }
        } catch (Exception e) {
            throw new ServerException(e.getMessage(), headers);
        }
        return new ResponseEntity<>(new BodyResponseWithTime<>("Create Success", HttpStatus.CREATED.value()), headers, HttpStatus.CREATED);
    }

    @PostMapping("/sendOTP")
    public ResponseEntity<BodyResponseWithTime<Object>> generateOTP(HttpSession session) {
        try {
            int user = (int) session.getAttribute("user");
            Status s = otpService.sendOTP(user);
            session.setAttribute("otpVerified", true);
            return new ResponseEntity<>(new BodyResponseWithTime<>("SEND SUCCESS", HttpStatus.CREATED.value()), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new BodyResponseWithTime<>("Please login to continue", HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping("/resendOTP")
    public ResponseEntity<BodyResponseWithTime<Object>> resendOTP(HttpSession session) {
        try {
            int user = (int) session.getAttribute("user");
            Status s = otpService.resendOTP(user);
            switch (s) {
                //Redirect to send page
                case NOT_FOUND -> {
                    return new ResponseEntity<>(new BodyResponseWithTime<>("OTP send again to your mail", HttpStatus.CREATED.value()), HttpStatus.CREATED);
                }
                case SUCCESS -> {
                    session.setAttribute("otpVerified", true);
                    return new ResponseEntity<>(new BodyResponseWithTime<>("SEND SUCCESS", HttpStatus.CREATED.value()), HttpStatus.CREATED);
                }
                case ERROR -> {
                    return new ResponseEntity<>(new BodyResponseWithTime<>("Please check your mail again", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
                }
                default -> {
                    return new ResponseEntity<>(new BodyResponseWithTime<>("Account not found", HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
                }
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new BodyResponseWithTime<>("Please login to continue", HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping("/recover")//redirect itself
    public ResponseEntity<Object> recoveryPassword(HttpSession session,@RequestBody createAccountDTO accountDto) {
        if(session!=null){
            session.invalidate();
        }
        try {
            State<AccountDto> res = otpService.sendOTPRecovery(accountDto.getEmail());
            if (Objects.requireNonNull(res.getStatus()) == Status.NOT_FOUND) {
                return new ResponseEntity<>(new BodyResponseWithTime<>("Account Not Found. Stay on page", HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new BodyResponseWithTime<>("SEND SUCCESS.Move to verify page", HttpStatus.CREATED.value()), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }
    @PostMapping("/verifyOTP")
    public ResponseEntity<Object> verifyOTP(HttpSession session, @RequestHeader int code) {
        try {
            Boolean otpVerified = (Boolean) session.getAttribute("otpVerified");
            if(!otpVerified){return new ResponseEntity<>(new BodyResponseWithTime<>("Please request to send OTP", HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);}
            int user = (int) session.getAttribute("user");
            Status s = otpService.verifyOTP(user, code);
            switch (s) {
                case ERROR -> {
                    return new ResponseEntity<>(new BodyResponseWithTime<>("Invalid OTP", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
                }
                case SUCCESS -> {
                    session.removeAttribute("otpVerified");
                    return new ResponseEntity<>(new BodyResponseWithTime<>("Verify Success", HttpStatus.OK.value()), HttpStatus.OK);
                }
                case NOT_FOUND -> {
                    return new ResponseEntity<>(new BodyResponseWithTime<>("Please resend OTP again", HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
                }
                case OUT_DATE -> {
                    return new ResponseEntity<>(new BodyResponseWithTime<>("OutDateOTP. Please resend OTP again", HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
                }
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new BodyResponseWithTime<>("Have not seen OTP yet", HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
        }
        return null;
    }
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        try {
            int user = (int) session.getAttribute("user");
            AccountDto userDto = service.findUser(user);
            String annouce = "Log out " + userDto.username();
            session.invalidate();
            return annouce;  // Redirect to login page
        } catch (Exception e) {
            return "Please login to continue";
        }

    }
}
//if(http!=null){return new ResponseEntity<>(new BodyResponseWithTime<>("You have been login", HttpStatus.CREATED.value()), HttpStatus.CREATED);}