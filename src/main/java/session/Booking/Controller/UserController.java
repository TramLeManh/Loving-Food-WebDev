package session.Booking.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import session.Booking.BookingService;
import session.Booking.DTO.BookingDecisionResponseDTO;
import session.Booking.DTO.CreateBookTableDTO;
import session.Booking.DTO.bookTableDTO;
import session.Booking.Model.BookingDecision;
import session.utils.Enum.BookingStatus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class UserController {


    private final BookingService bookingService;

    public UserController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/getBookingResponseDetail")
    public ResponseEntity<Map<String, Object>> getUserOrderResponse(@RequestParam int decision_id) {
        Map<String, Object> response = new LinkedHashMap<>();
        BookingDecision res = bookingService.getDetailDecision(decision_id);
        if(res == null){
            response.put("message", "BookingDecision not been response yet.");
            return ResponseEntity.badRequest().body(response);
        }
        BookingDecisionResponseDTO bookingDecision = BookingDecisionResponseDTO.fromEntity(res);
        response.put("message", "Retrieved booking adminDecision for decision_id: " + decision_id);
        response.put("data", bookingDecision);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/getUserBooking")
    public ResponseEntity<Object> getUserBooking(@RequestParam int user_id, @RequestParam(required = false) Integer status, @RequestParam(required = false) Integer booking_id) {
        Map<String, Object> response = new LinkedHashMap<>();
        BookingStatus bookingStatus = null;
        if (status != null) {
            try {
                bookingStatus = BookingStatus.values()[status];
            } catch (ArrayIndexOutOfBoundsException e) {
                response.put("message", "Invalid BookingStatus value provided.");
                return ResponseEntity.badRequest().body(response);
            }
        }
        List<bookTableDTO> bookings = bookingService.getUserBooking(user_id, bookingStatus);
        response.put("message", "Retrieved bookings for user with status: " + bookingStatus);
        response.put("data", bookings);
        bookings.stream().filter(booking -> Objects.equals(booking.getBookingId(), booking_id));
        return ResponseEntity.ok(response);
    }

    @Transactional
    @PostMapping("/updateBooking")
    public ResponseEntity<Map<String, Object>> updateBooking(@RequestBody CreateBookTableDTO book) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            bookingService.updateUserBooking(book.getBookingId(), book.getName(), book.getPhone(), book.getTime(), book.getNumber_of_guests(), book.getNote());
            response.put("message", "Booking updated successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    @PostMapping("/createBookingOrder")
    public CreateBookTableDTO createUserBookingOrder(@RequestBody CreateBookTableDTO book) {
        bookingService.createUserBooking(CreateBookTableDTO.toEntity(book));
        return book;
    }

    @PostMapping("/deleteBooking")
    public void deleteUserBooking(@RequestParam int booking_id) {
        bookingService.deleteUserBooking(booking_id);
    }




}