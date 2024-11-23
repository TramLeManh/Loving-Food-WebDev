package session.Booking.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import session.Booking.BookingService;
import session.Booking.DTO.BookingResponse;
import session.Booking.DTO.bookTableDTO;
import session.Booking.DTO.createDecisionDTO;
import session.Booking.DTO.updateResponseDTO;
import session.Booking.Model.BookingDecision;
import session.utils.Enum.BookingStatus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
public class AdminRestController {
    private final BookingService bookingService;

    public AdminRestController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/viewAllDecision")
    public List<BookingResponse> getAdminDecision(@RequestParam int owner_id, @RequestParam(required = false) int type) {
        BookingStatus[] status = BookingStatus.values();
        return bookingService.getAdminDecision(owner_id, status[type]);
    }

    @GetMapping("/viewAdminBookingOrder")
    public ResponseEntity<Object> getAdminBooking(@RequestParam int user_id, @RequestParam(required = false) Integer status, @RequestParam(required = false) Integer booking_id) {
        Map<String, Object> response = new LinkedHashMap<>();
        BookingStatus bookingStatus=null;
        if (status != null) {
            try {
                bookingStatus = BookingStatus.values()[status];
            } catch (ArrayIndexOutOfBoundsException e) {
                response.put("message", "Invalid BookingStatus value provided.");
                return ResponseEntity.badRequest().body(response);
            }
        }
        //Due to native query, we need to pass the status as a string
        response.put("message", "Retrieved bookings for user with status: " + bookingStatus);
        List<bookTableDTO> bookings = bookingService.getOwnerBooking(user_id, bookingStatus.toString());
        response.put("data", bookings);
        bookings.stream().filter(booking -> Objects.equals(booking.getBookingId(), booking_id)).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/decision")
    public ResponseEntity<Object> getDecision(@RequestParam int decision_id, @RequestParam String action) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            if (action.equalsIgnoreCase("view")) {
                BookingDecision decision = bookingService.getDetailDecision(decision_id);
                return ResponseEntity.ok(decision);
            }
            response.put("message", "Invalid action provided. Use 'view', 'create', or 'update'.");
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            response.put("message", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Transactional
    @PostMapping("/decision")
    public ResponseEntity<Object> createDecision(
            @RequestParam String action,
            @RequestBody(required = false) createDecisionDTO decisionDTO,
            @RequestParam(required = false) Integer booking_id) {

        Map<String, Object> response = new LinkedHashMap<>();

        try {
            if (action.toLowerCase().equals("create")) {
                if (decisionDTO == null) {
                    response.put("message", "Decision data is missing.");
                    return ResponseEntity.badRequest().body(decisionDTO);
                }
                if (bookingService.isDecisionExist(decisionDTO.getBooking_id())) {
                    response.put("message", "BookingDecision already exists.");
                    return ResponseEntity.badRequest().body(response);
                }
                bookingService.createDecision(decisionDTO.getOwner_id(), decisionDTO.getBooking_id(), decisionDTO.getNote(), decisionDTO.getStatus());
                response.put("message", "BookingDecision created successfully.");
                return ResponseEntity.ok(response);
            }
            response.put("message", "Invalid action provided. Use 'view', 'create', or 'update'.");
            return ResponseEntity.badRequest().body(response);
        } catch (IllegalArgumentException e) {
            response.put("message", "Invalid BookingStatus value provided.");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("message", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Transactional
    @PutMapping("/decision")
    public ResponseEntity<Object> updateDecision(
            @RequestParam String action,
            @RequestBody(required = false) updateResponseDTO updateResponseDTO,
            @RequestParam(required = false) Integer booking_id) {
        Map<String, Object> response = new LinkedHashMap<>();
        System.out.println(updateResponseDTO.getNote());
        try {
            if (action.equalsIgnoreCase("update")) {
                if (!bookingService.isDecisionExist(booking_id)) {
                    response.put("message", "BookingDecision does not exist.");
                    return ResponseEntity.badRequest().body(response);
                }
                bookingService.updateDecision(booking_id, updateResponseDTO.getStatus(), updateResponseDTO.getNote());
                response.put("message", "Updated successfully.");
                return ResponseEntity.ok(response);
            }
            response.put("message", "Error");
            return ResponseEntity.badRequest().body(response);
        } catch (IllegalArgumentException e) {
            response.put("message", "Invalid BookingStatus value provided.");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("message", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}