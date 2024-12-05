package session.Booking.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import session.Booking.BookingService;
import session.Booking.DTO.CreateBookTableDTO;
import session.Booking.DTO.bookTableDTO;
import session.Restaurant.RestaurantService;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
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

//    @GetMapping("/getDetailBooking/{booking_id}")
//    public String getUserOrderResponse(HttpSession session, Model model, @PathVariable Integer booking_id, @RequestParam String action) {
//        Integer user_id = (Integer) session.getAttribute("user_id");
//        bookTableDTO book = bookingService.getOwnerBookingDetail(booking_id);
//        if(book==null||user_id == null){
//            return "error";
//        }
//        if(Objects.equals(action, "edit")&& Objects.equals(book.getStatus(), "PENDING")){
//            model.addAttribute("bookingDecision", book);
//            return "bookDetailUpdate";//Page này cho phép update
//        }
//        else if(Objects.equals(action, "view")){
//            model.addAttribute("bookingDecision", book);
//            return "bookDetail";//Page này view kèm response if cos
//        }
//        return "error";
//    }



    @GetMapping("/createOrder/{restaurant_id}")
    public String homePage(@PathVariable String restaurant_id, int booking_id, Model model) {
        model.addAttribute("restaurant_id", restaurant_id);//De ẩn thông tin restaurant_id
        return "form"; // Renders home.html or home.jsp from templates folder
    }

    @GetMapping("/getUserBooking")
    public String getUserBooking(HttpSession session, @RequestParam(required = false) Integer status, Model model) {
        Integer user_id = (Integer) session.getAttribute("user");

        if (user_id == null) {
            return "error";
        }
        List<bookTableDTO> bookings = bookingService.getUserBooking(user_id, status);
        model.addAttribute("bookingTable", bookings);
        return "test";
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Transactional
    @PostMapping(value = "/updateBooking", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> updateBooking(@ModelAttribute CreateBookTableDTO book) {
        System.out.println();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(book.getTime(), formatter);

        bookingService.updateUserBooking(book.getBooking_id(), book.getName(), book.getPhone(), dateTime.toString(), book.getNumber_of_guests(), book.getNote()
                );

        return ResponseEntity.ok("Success") ;
    }

    @PostMapping("/createOrder/{restaurant_id}")
    public String createUserBookingOrder(HttpSession session, @RequestBody CreateBookTableDTO book, @PathVariable int restaurant_id) {
        String user_id = (String) session.getAttribute("user_id");
        bookingService.createUserBooking(CreateBookTableDTO.toEntity(book,user_id,restaurant_id));
        return "index";//go to index
    }
    @DeleteMapping("/deleteBooking/{booking_id}")
    public ResponseEntity<Object> deleteUserBooking(@PathVariable Integer booking_id) {
        bookingService.deleteUserBooking(booking_id);;
        return ResponseEntity.ok("Booking deleted successfully");
    }



}