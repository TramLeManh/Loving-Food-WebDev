package session.Booking.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import session.Booking.BookingService;
import session.Booking.DTO.BookingResponse;
import session.Booking.DTO.bookTableDTO;
import session.Booking.DTO.createDecisionDTO;
import session.Booking.DTO.updateResponseDTO;
import session.Booking.Model.BookingDecision;
import session.Restaurant.DTO.createRestaurantDTO;
import session.Restaurant.RestaurantService;
import session.utils.Enum.BookingStatus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Controller
@RequestMapping("/admin")
public class AdminRestController {
    private final BookingService bookingService;
    private final RestaurantService restaurantService;

    public AdminRestController(BookingService bookingService, RestaurantService restaurantService) {
        this.bookingService = bookingService;
        this.restaurantService = restaurantService;
    }



    @GetMapping("/getBookingOrder")
    public String getAdminBooking( @RequestParam(required = false) Integer status, Model model) {
        //Due to native query, we need to pass the status as a string
        List<bookTableDTO> orders = bookingService.getOwnerBooking(6441, status);
        model.addAttribute("orders", orders);
        System.out.println(orders.get(0).getBookingId());
        model.addAttribute("currentStatus", status);
        return "ownerBookingOrder";
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
    @PostMapping("/decision/{action}")
    public ResponseEntity<Object> createDecision(
            HttpSession session,
            @RequestBody(required = false) createDecisionDTO decisionDTO,
            @PathVariable String action) {

//        Integer user_id = (Integer) session.getAttribute("user");
        try {
            if (action.equalsIgnoreCase("create")) {
                bookingService.createDecision(6441, decisionDTO.getBooking_id(), decisionDTO.getNote(), decisionDTO.getStatus());
                return ResponseEntity.ok("Decision created successfully.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred: " + e.getMessage());
        }
        return ResponseEntity.badRequest().body("Invalid action provided. Use 'view', 'create', or 'update'.");
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
    @PostMapping("/createRestaurant")
    public ResponseEntity<Map<String, Object>> insertRestaurant(HttpSession session, @RequestBody createRestaurantDTO createRestaurantDTO) {
        int owner_id = 8242;
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            response.put("message", "Insert restaurant successfully");
            restaurantService.createRestaurant(createRestaurantDTO,owner_id);
            return ResponseEntity.ok().body(response);
        }catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

    }
}