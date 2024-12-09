package session.Restaurant;

import jakarta.servlet.http.HttpSession;
import org.springframework.cache.annotation.AbstractCachingConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import session.Account.Account;
import session.Booking.BookingService;
import session.Booking.DTO.bookTableDTO;

import java.util.List;
@RequestMapping("/restaurant")
@Controller
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final BookingService bookingService;
    private final RestaurantDAO restaurantDAO;
    public RestaurantController(RestaurantService restaurantService, BookingService bookingService, RestaurantDAO restaurantDAO) {
        this.restaurantService = restaurantService;
        this.bookingService = bookingService;
        this.restaurantDAO = restaurantDAO;
    }
//    @RequestMapping("/get")
//    public String restaurant(HttpSession session, Model model, @RequestParam("category") String category) {
//        List<Restaurant> list_restaurant = restaurantService.getRestaurant(null,category);
//        model.addAttribute("list_restaurant",list_restaurant);
//        model.addAttribute("category", category);
//        return "category_restaurants";
//    }
    @RequestMapping("/get/{res}")
    public ResponseEntity<Object> test(@PathVariable Integer res) {
        Account account = restaurantDAO.getUserByRestaurant(res);
        return ResponseEntity.ok(account);
    }
    @RequestMapping("/test")
    public ResponseEntity<Object> test() {
        List<Restaurant> account = restaurantService.getRestaurant(null,null);
        return ResponseEntity.ok(account);
    }

}