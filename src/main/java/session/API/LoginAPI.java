package session.API;


import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import session.DTO.UserDto;
import session.service.AccountService;
import session.utils.State;
import session.utils.Status;
@Controller
public class LoginAPI {
    private final AccountService service;
    public LoginAPI(AccountService service) {
        this.service = service;
    }
    @PostMapping("/account/login")
    public String loginTest(HttpSession session, @RequestParam("username") String username, @RequestParam("password") String password, RedirectAttributes redirectAttributes) {
        State<UserDto> res = service.login(username, password);//Server check
        if (res.getStatus() != Status.SUCCESS) {
            redirectAttributes.addFlashAttribute("state", res.getStatus().toString());//Set state
            redirectAttributes.addFlashAttribute("username", username);
            return "redirect:/account/login";
        }
        //Login thành công se tao session store user id.
        session.setAttribute("user", res.getData().id());
        redirectAttributes.addFlashAttribute("state", res.getStatus().toString());
        return "redirect:/account/login";
    }
}