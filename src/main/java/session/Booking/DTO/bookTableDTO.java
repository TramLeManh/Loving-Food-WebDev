package session.Booking.DTO;

import lombok.Getter;
import lombok.Setter;
import session.Booking.Model.BookingDecision;
import session.Booking.Model.TableBooking;
import session.utils.Enum.BookingStatus;
import session.utils.timeFormat;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
public class bookTableDTO {
    private int bookingId;
    private int restaurant_id;
    private String name;
    private String phone;
    private int number_of_guests;
    private BookingStatus status;
    private String note;
    private String booking_date;
    private BookingDecisionResponseDTO adminDecision;
    private String updated_at;
    public static bookTableDTO fromEntity(TableBooking booking, BookingDecision decision) {
        bookTableDTO responseDTO = new bookTableDTO();
        responseDTO.setBookingId(booking.getBookingId());
        responseDTO.setRestaurant_id(booking.getRestaurantId());
        responseDTO.setName(booking.getName());
        responseDTO.setPhone(booking.getPhoneNumber());
        responseDTO.setNumber_of_guests(booking.getNumOfGuests());
        responseDTO.setStatus(booking.getStatus());
        responseDTO.setNote(booking.getNotes());
        responseDTO.setBooking_date(timeFormat.format(booking.getBooking()));
        responseDTO.setAdminDecision(decision == null ? null : BookingDecisionResponseDTO.fromEntity(decision));
        responseDTO.setUpdated_at(timeFormat.format(booking.getUpdate()));
        return responseDTO;
    }
    public static List<bookTableDTO> fromEntity(List<TableBooking> bookings, List<BookingDecision> decisions) {
        return bookings.stream().map(booking -> {
            BookingDecision decision = decisions.stream().filter(d -> Objects.equals(d.getBookingInformation().getBookingId(), booking.getBookingId())).findFirst().orElse(null);
            return bookTableDTO.fromEntity(booking, decision);
        }).collect(Collectors.toList());
    }


}