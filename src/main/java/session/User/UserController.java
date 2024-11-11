package session.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    private final UserService service;
    public UserController(UserService service) {
        this.service = service;
    }
    @GetMapping("/account/login")
    public String login(HttpSession session, Model model, HttpServletResponse response) {
        // Disable cache tranh tro lai login sau khi dang nhap thanh cong
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        if (session.getAttribute("user") != null&&!model.containsAttribute("state")) {
            return "redirect:/index";
        }
        return "form";
    }
    @GetMapping("/verifyEmail")
    public String recover() {
        return "verify";
    }
    @GetMapping("/register")
    public String register(HttpSession session, Model model, HttpServletResponse response) {
        String email = (String) session.getAttribute("email");
        if (email != null) {
            model.addAttribute("email", email);
        }
        return "register";
    }
}