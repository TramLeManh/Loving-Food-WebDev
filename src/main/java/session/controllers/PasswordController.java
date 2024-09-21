package session.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import session.responseHandler.Exception.ServerException;
import session.utils.Service.PasswordService;
import session.utils.Status;

@RestController
@RequestMapping("/password")
public class PasswordController {
    private final PasswordService service;

    public PasswordController(PasswordService service) {
        this.service = service;
    }

    @RequestMapping("/{id}")
    public String home(HttpSession session) {
        try {
            return service.getPassword((int) session.getAttribute("user"));
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public String update(HttpSession session, @RequestBody String input) {
        try {
            int user = (int) session.getAttribute("user");
            Status s = service.updatePassword(user, input);
            switch (s) {
                case SUCCESS:
                    return "Password updated successfully";
                case ACCOUNT_NOT_FOUND:
                    return "Account not found";
            }
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
        return "Error";
    }


}