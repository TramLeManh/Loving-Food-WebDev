package session.Booking.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import session.Booking.BookingService;
import session.Booking.DTO.CreateBookTableDTO;
import session.Booking.DTO.bookTableDTO;
import session.Restaurant.RestaurantService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {
    private final BookingService bookingService;
    public UserController(BookingService bookingService, RestaurantService restaurantService) {
        this.bookingService = bookingService;

    }
//    @GetMapping("/getUserBooking")
//    public String getUserBooking(HttpSession session, @RequestParam(required = false) Integer status, Model model) {
//        Integer user_id = (Integer) session.getAttribute("user_id");
//        if (user_id == null) {
//            return "error";
//        }
//        List<bookTableDTO> bookings = bookingService.getUserBooking(user_id, status);
//        model.addAttribute("bookingTable", bookings);
//        return "userBooking";
//    }
//    @PostMapping("/deleteBooking/{booking_id}")
//    public void deleteUserBooking(@PathVariable Integer booking_id) {
//        bookingService.deleteUserBooking(booking_id);
//
//    }

    @GetMapping("/getDetailBooking/{booking_id}")
    public String getUserOrderResponse(HttpSession session, Model model, @PathVariable Integer booking_id, @RequestParam String action) {
        Integer user_id = (Integer) session.getAttribute("user_id");
        bookTableDTO book = bookingService.getOwnerBookingDetail(booking_id);
        if(book==null||user_id == null){
            return "error";
        }
        if(Objects.equals(action, "edit")&& Objects.equals(book.getStatus(), "PENDING")){
            model.addAttribute("bookingDecision", book);
            return "bookDetailUpdate";//Page này cho phép update
        }
        else if(Objects.equals(action, "view")){
            model.addAttribute("bookingDecision", book);
            return "bookDetail";//Page này view kèm response if cos
        }
        return "error";
    }



    @GetMapping("/createOrder/{restaurant_id}")
    public String homePage(@PathVariable String restaurant_id, int booking_id, Model model) {
        model.addAttribute("restaurant_id", restaurant_id);//De ẩn thông tin restaurant_id
        return "form"; // Renders home.html or home.jsp from templates folder
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    @Transactional
    @PostMapping("/updateBooking/{booking_id}")
    public String updateBooking(@RequestBody CreateBookTableDTO book, @PathVariable Integer booking_id) {
        bookingService.updateUserBooking(booking_id, book.getName(), book.getPhone(), book.getTime(), book.getNumber_of_guests(), book.getNote());
        return "/getDetailBooking/{booking_id}";//refresh lại trang
    }

    @PostMapping("/createOrder/{restaurant_id}")
    public String createUserBookingOrder(HttpSession session, @RequestBody CreateBookTableDTO book, @PathVariable int restaurant_id) {
        String user_id = (String) session.getAttribute("user_id");
        bookingService.createUserBooking(CreateBookTableDTO.toEntity(book,user_id,restaurant_id));
        return "index";//go to index
    }

}