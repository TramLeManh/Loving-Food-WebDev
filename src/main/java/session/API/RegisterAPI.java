package session.API;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import session.DTO.UserDto;
import session.DTO.createAccountDTO;
import session.responseHandler.Exception.ServerException;
import session.service.AccountService;
import session.service.OtpService;
import session.utils.State;

import session.utils.generateSessionToken;



/**
 * RegisterAPI
 */
@Controller
public class RegisterAPI {
    private final AccountService service;
    private final OtpService otpService;
    public RegisterAPI(AccountService service, OtpService otpService) {
        this.service = service;
        this.otpService = otpService;
    }
    @PostMapping(value = "/register")
    public String processForm(@ModelAttribute("formData") createAccountDTO formData, Model model) throws Exception {
        // Process formData (which contains username, email, password)
        try {
            State<UserDto> res = service.createAccount(formData);
            switch (res.getStatus()) {
                case SUCCESS:
                    model.addAttribute("message", "User created successfully");
                    //return new page
                    break;
                case EXIST_USERNAME:
                    model.addAttribute("message", "User already exists");
                    break;
                case EXIST_EMAIL:
                    model.addAttribute("message", "Email already exists");
                    break;
            }
            return "register";
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }
    @PostMapping(value = "/verifyEmail")
    public String verifyEmail(HttpSession session, @RequestParam String email, RedirectAttributes redirectAttributes) {
        try {
            if (service.isEmailExist(email)) {
                redirectAttributes.addFlashAttribute("state", "error");//Set state
                return "redirect:/verifyEmail";
            }
            //generate session token
            String token = generateSessionToken.get();
            //set token for that session
            System.out.println(token + " " + email);
            session.setAttribute("sessionToken",token);//set token for verify
            session.setMaxInactiveInterval(1800);//Set session time out  minutes
            redirectAttributes.addFlashAttribute("email", email);
            session.setAttribute("email", email);
//          redirectAttributes.addFlashAttribute("isRegister", true);
            return "redirect:/verifyOTP/register/" + token;
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }

    }
}