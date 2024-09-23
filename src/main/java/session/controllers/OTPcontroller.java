package session.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import session.DTO.UserDto;
import session.DTO.createAccountDTO;
import session.responseHandler.BodyResponseWithTime;
import session.responseHandler.Exception.ServerException;
import session.service.AccountService;
import session.service.OtpService;
import session.utils.Service.PasswordService;
import session.utils.State;
import session.utils.Status;
import session.utils.generateSessionToken;

import java.util.Objects;

@RestController
@RequestMapping("/account")
public class OTPcontroller {
    private final OtpService otpService;
    private final PasswordService service;

    public OTPcontroller(OtpService otpService, PasswordService service) {
        this.otpService = otpService;
        this.service = service;
    }
    @PostMapping("/recover")//redirect itself
    public ResponseEntity<Object> recoveryPassword(HttpSession session,@RequestBody createAccountDTO accountDto) {
        try {
            //generate session token
            String token = generateSessionToken.get();
            State<UserDto> res = otpService.sendOTPRecovery(token,accountDto.getEmail());
            if (Objects.requireNonNull(res.getStatus()) == Status.NOT_FOUND) {
                return new ResponseEntity<>(new BodyResponseWithTime<>("User Not Found. Stay on page", HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
            }
            //set token for that session
            session.setAttribute("sessionToken",token);//set token for verify
            session.setMaxInactiveInterval(1800);//Set session time out  minutes
            return new ResponseEntity<>(new BodyResponseWithTime<>("SEND SUCCESS.Move to verify page", HttpStatus.CREATED.value()), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }
    @PostMapping("/verifyOTP")
    public ResponseEntity<Object> verifyOTP(HttpSession session, @RequestHeader int code,@RequestParam String sessionToken) {
        try {
            String token = (String) session.getAttribute("sessionToken");
            System.out.println(sessionToken);

            if(!sessionToken.equals(token)){
                return new ResponseEntity<>(new BodyResponseWithTime<>("Bad request", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
            }
            State<Integer> s = otpService.verifyOTP(sessionToken, code);
            switch (s.getStatus()) {
                case ERROR -> {
                    return new ResponseEntity<>(new BodyResponseWithTime<>("Invalid OTP", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
                }
                case SUCCESS -> {
                    session.setAttribute("user_id",s.getData());
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

            return new ResponseEntity<>(new BodyResponseWithTime<>(e.getMessage(), HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
        }
        return null;
    }
    @PutMapping("/changePassword")
    public String update(HttpSession session,@RequestHeader String input) {
        try {
            int user = (int) session.getAttribute("user_id");
            Status s = service.updatePassword(user, input);
            switch (s) {
                case SUCCESS:
                    return "Password updated successfully";
                case ACCOUNT_NOT_FOUND:
                    return "User not found";
            }
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
        return "Error";
    }
    @PutMapping("/resetPassword")
    public String reset(HttpSession session, @RequestHeader String input,@RequestParam String sessionToken) {
        try {
            int user = (int)session.getAttribute("user_id");
            Status s = service.updatePassword(user, input);
            switch (s) {
                case SUCCESS:
                    session.invalidate();
                    return "Password updated successfully";
                case ACCOUNT_NOT_FOUND:
                    return "User not found";
            }
        } catch (Exception e) {
            return String.valueOf(new ResponseEntity<>(new BodyResponseWithTime<>("404 error", HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED));
        }
        return "Error";
    }

}