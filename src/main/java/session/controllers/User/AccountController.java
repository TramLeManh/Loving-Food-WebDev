package session.controllers.User;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import session.DTO.UserDto;
import session.service.AccountService;
import session.utils.State;
import session.utils.Status;
@Controller
public class AccountController {
    private final AccountService service;

    public AccountController(AccountService service) {
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
        //Get username from previous request if password error
        if (model.containsAttribute("username")) {
            model.addAttribute("username", model.getAttribute("username"));
        }
        return "form";
    }

}