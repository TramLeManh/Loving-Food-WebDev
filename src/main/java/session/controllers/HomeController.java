package session.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import session.model.District;
import session.service.AccountService;
import session.DTO.AccountDto;
import session.DTO.createAccountDTO;
import session.service.RestaurantService;
import session.responseHandler.Exception.ServerException;
import session.utils.State;

import java.util.List;


@Controller
public class HomeController {
    private final AccountService service;
    private final RestaurantService restaurantService;
    public HomeController(AccountService service, RestaurantService restaurantService) {
        this.service = service;
        this.restaurantService = restaurantService;
    }
    @RequestMapping("/get/{id}")
    public String home(@PathVariable int id, Model model) {
        try {
            //View All account base on AccountDto format
            AccountDto res = service.findUser(id);
            System.out.println("id is" + res.id());
            model.addAttribute("user", res);
            return "person";  // user.html will be returned
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }
    //Load
    @RequestMapping("/register")
    public String showForm(Model model) {
        try {
            model.addAttribute("formData",  new createAccountDTO());
            return "register"; // refers to src/main/resources/templates/form.html
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }
//    @PostMapping(value = "/submit")
    @PostMapping(value = "/register")
    public String processForm(@ModelAttribute("formData") createAccountDTO formData, Model model) throws Exception {
        // Process formData (which contains username, email, password)
       try{
              State<AccountDto> res = service.createAccount(formData);
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

    @RequestMapping(value = "/forgotpass/send", method = RequestMethod.POST)
    public String passRecovery(@ModelAttribute("formData") createAccountDTO formData, Model model) throws Exception {
        // Process formData (which contains username, email, password)
        try {
            State<AccountDto> res = service.createAccount(formData);
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

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping("/blog")
    public  String blog(){
        return "blog";
    }

    @RequestMapping("/index")
    public  String index(Model model){
        List<District> list_category = restaurantService.getDistrict();;
        model.addAttribute("list_district" , list_category );
        return "index";
    }
    @RequestMapping("/contact")
    public  String contact(){
        return "contact";
    }
    @RequestMapping("/service")
    public  String service(){
        return "service";
    }
    @RequestMapping("/shop")
    public  String shop(){
        return "shop";
    }

}