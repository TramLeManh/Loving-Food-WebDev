package session.Booking.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import session.Booking.BookingService;
import session.Booking.DTO.CreateBookTableDTO;
import session.Booking.DTO.bookTableDTO;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {
    private final BookingService bookingService;
    public UserController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

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

    @GetMapping("/getUserBooking")
    public String getUserBooking(HttpSession session, @RequestParam(required = false) Integer status, Model model) {
        Integer user_id = (Integer) session.getAttribute("user_id");
        if (user_id == null) {
            return "error";
        }
        List<bookTableDTO> bookings = bookingService.getUserBooking(user_id, status);
        model.addAttribute("bookingTable", bookings);
        return "userBooking";
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @PostMapping("/deleteBooking/{booking_id}")
    public void deleteUserBooking(@PathVariable Integer booking_id) {
        bookingService.deleteUserBooking(booking_id);
    }

    @Transactional
    @PostMapping("/updateBooking/{booking_id}")
    public String updateBooking(@RequestBody CreateBookTableDTO book, @PathVariable Integer booking_id) {
        bookingService.updateUserBooking(booking_id, book.getName(), book.getPhone(), book.getTime(), book.getNumber_of_guests(), book.getNote());
        return "/getDetailBooking/{booking_id}";//refresh lại trang
    }

    @PostMapping("/createBookingOrder")
    public CreateBookTableDTO createUserBookingOrder(@RequestBody CreateBookTableDTO book) {
        bookingService.createUserBooking(CreateBookTableDTO.toEntity(book));
        return book;
    }


}