package session.controllers;

//error redirect itself

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import session.DTO.UserDto;
import session.DTO.createAccountDTO;
import session.responseHandler.BodyResponseWithTime;
import session.responseHandler.Exception.ServerException;
import session.service.AccountService;
import session.service.OtpService;
import session.utils.State;
import session.utils.Status;

/**
 * RestController
 */

@org.springframework.web.bind.annotation.RestController

public class RestController {
    private final AccountService service;
    private final OtpService otpService;

    public RestController(AccountService service, OtpService otpService) {
        this.service = service;
        this.otpService = otpService;
    }
    //admin view all account

    @PostMapping("/get")
    public ResponseEntity<Object> getAll() {
        try {
            //View All account base on UserDto format
            return new ResponseEntity<>(service.getAllAccount(), new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            throw new ServerException(e.getMessage(), new HttpHeaders());
        }
    }
    //Để homecontroller
    @PostMapping("/booking")
    public String postLogin() {
        return "login";
    }

    //
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        try {
            int user = (int) session.getAttribute("user");
            UserDto userDto = service.findUser(user);
            String annouce = "Log out " + userDto.username();
            session.invalidate();
            return annouce;  // Redirect to login page
        } catch (Exception e) {
            return "Please login to continue";
        }

    }

    //
    @RequestMapping("account/getInformation")
    public ResponseEntity<Object> findUserById(HttpSession session) {
        try {
            int id = (int) session.getAttribute("user");
            UserDto res = service.findUser(id);
            return new ResponseEntity<>(new BodyResponseWithTime<>(res, HttpStatus.OK.value()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new BodyResponseWithTime<>("Please login to continue", HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
        }
    }

    //Create User
    @PostMapping("account/register")
    public ResponseEntity<Object> create(@RequestBody createAccountDTO accountDto) {
        HttpHeaders headers = new HttpHeaders();
        try {
            State<UserDto> res = service.createAccount(accountDto);
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
    //Send OTP


}
//if(http!=null){return new ResponseEntity<>(new BodyResponseWithTime<>("You have been login", HttpStatus.CREATED.value()), HttpStatus.CREATED);}