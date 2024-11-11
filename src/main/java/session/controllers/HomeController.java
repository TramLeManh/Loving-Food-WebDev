package session.controllers;


import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import session.User.DTO.UserDTO;
import session.model.District;
import session.responseHandler.Exception.ServerException;
import session.User.UserService;
import session.Restaurant.RestaurantService;

import java.util.List;


@Controller
public class    HomeController {
    private final UserService service;
    private final RestaurantService restaurantService;

    public HomeController(UserService service, RestaurantService restaurantService) {
        this.service = service;
        this.restaurantService = restaurantService;
    }

    @RequestMapping("/get/{id}")
    public String home(@PathVariable int id, Model model) {
        try {
            //View All account base on UserDTO format
            UserDTO res = service.findUser(id);
            System.out.println("id is" + res.id());
            model.addAttribute("user", res);
            return "person";  // user.html will be returned
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }






    //    @PostMapping(value = "/submit")


//    @RequestMapping(value = "/forgotpass/send", method = RequestMethod.POST)
//    public String passRecovery(@ModelAttribute("formData") createAccountDTO formData, Model model) throws Exception {
//        // Process formData (which contains username, email, password)
//        try {
//            State<UserDTO> res = service.createAccount(formData);
//            switch (res.getStatus()) {
//                case SUCCESS:
//                    model.addAttribute("message", "User created successfully");
//
//                    //return new page
//                    break;
//                case EXIST_USERNAME:
//                    model.addAttribute("message", "User already exists");
//                    break;
//                case EXIST_EMAIL:
//                    model.addAttribute("message", "Email already exists");
//                    break;
//            }
//            return "register";
//        } catch (Exception e) {
//            throw new ServerException(e.getMessage());
//        }
//
//    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping("/blog")
    public String blog() {
        return "blog";
    }

    @RequestMapping("/index")
    public String index(HttpSession session, Model model) {
        List<District> list_category = restaurantService.getDistrict();//server


        model.addAttribute("list_district", list_category);
        try {
            int id = (int) session.getAttribute("user");
            UserDTO res = service.findUser(id);
            model.addAttribute("user_email", res.email());
        } catch (Exception e) {
            model.addAttribute("user", null);
        }
        return "index";
    }

    @RequestMapping("/contact")
    public String contact() {
        return "contact";
    }

    @RequestMapping("/service")
    public String service() {
        return "service";
    }

    @RequestMapping("/shop")
    public String shop() {
        return "shop";
    }


}