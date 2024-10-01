package session.controllers.User;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class CreateController {
    @GetMapping("/verifyEmail")
    public String recover() {
        return "verify";
    }
    @GetMapping("/account/register")
    public String register(HttpSession session, Model model, HttpServletResponse response) {
        return "register";
    }
}