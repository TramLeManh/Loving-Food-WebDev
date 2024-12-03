package session.Restaurant;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/restaurant")
@Controller
public class RestaurantController {
    private final RestaurantService restaurantService;
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
    @RequestMapping("/get")
    public String restaurant(HttpSession session, Model model, @RequestParam("category") String category) {
        List<Restaurant> list_restaurant = restaurantService.getRestaurant(null,category);
        model.addAttribute("list_restaurant",list_restaurant);
        model.addAttribute("category", category);
        return "category_restaurants";
    }


}