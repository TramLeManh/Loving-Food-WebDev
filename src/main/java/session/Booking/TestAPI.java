package session.Booking;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import session.Booking.DTO.BookingDecisionResponseDTO;
import session.Booking.DTO.CreateBookTableDTO;
import session.Booking.Model.TableBooking;
import session.utils.Enum.BookingStatus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class TestAPI {


    private final BookingService bookingService;

    public TestAPI(BookingService bookingService) {
        this.bookingService=bookingService;
    }
    @GetMapping("/getBookingResponseDetail")
    public   ResponseEntity<Map<String, Object>>  getUserOrderResponse(@RequestParam int bookingId) {
        Map<String, Object> response = new LinkedHashMap<>();
        if(!bookingService.isDecisionExist(bookingId)){
            response.put("message", "BookingDecision not found.");
            return ResponseEntity.badRequest().body(response);
        }
        if(!bookingService.isDecisionPending(bookingId)){
            BookingDecisionResponseDTO bookingDecisions = BookingDecisionResponseDTO.fromEntity(bookingService.getUserDetailDecision(bookingId));
            response.put("message", "Retrieved booking decision for bookingId: " + bookingId);
            response.put("data", bookingDecisions);
            return ResponseEntity.ok(response);
        }
        response.put("message", "BookingDecision is postponed.");
        return ResponseEntity.badRequest().body(response);
    }
    @GetMapping("/getUserBooking")
    public ResponseEntity<Object> getUserBooking(@RequestParam int user_id, @RequestParam(required = false) String status, @RequestParam(required = false) Integer booking_id) {
        BookingStatus bookingStatus = null;
        Map<String, Object> response = new LinkedHashMap<>();

        if (status != null) {
            try {
                bookingStatus = BookingStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                response.put("message", "Invalid BookingStatus value provided.");
                return ResponseEntity.badRequest().body(response);
            }
        }
        List<TableBooking> bookings = bookingService.getUserBooking(user_id, status);
        if (bookingStatus == null) {
            response.put("message", "Retrieved all bookings for user.");
        } else {
            //Due to native query, we need to pass the status as a string
            response.put("message", "Retrieved bookings for user with status: " + bookingStatus);
        }
        if(booking_id != null){
            bookings = bookings.stream().filter(booking -> Objects.equals(booking.getBookingId(), booking_id)).toList();
        }
        response.put("data", bookings);
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
    @PostMapping("/deleteBooking")
    public void deleteUserBooking(@RequestParam int booking_id) {
        bookingService.deleteUserBooking(booking_id);
    }
    @PostMapping("/createBookingOrder")
    public CreateBookTableDTO createUserBookingOrder(@RequestBody CreateBookTableDTO book) {
        bookingService.createUserBooking(book.getBookingId(), book.getUser_id(), book.getRestaurant_id(), book.getName(), book.getPhone(), book.getTime(), book.getNumber_of_guests(), book.getNote());
        return book;
    }


}