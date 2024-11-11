package session.Booking.DTO;

import lombok.Getter;
import lombok.Setter;
import session.Booking.Model.TableBooking;
import session.utils.timeFormat;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class bookTableDTO {
    private int bookingId;
    private int restaurant_id;
    private String name;
    private String phone;
    private int number_of_guests;
    private String status;
    private String note;
    private String booking_date;
    private String updated_at;
    public static bookTableDTO fromEntity(TableBooking booking) {
        bookTableDTO responseDTO = new bookTableDTO();
        responseDTO.setBookingId(booking.getBookingId());
        responseDTO.setRestaurant_id(booking.getRestaurantId());
        responseDTO.setName(booking.getName());
        responseDTO.setPhone(booking.getPhoneNumber());
        responseDTO.setNumber_of_guests(booking.getNumOfGuests());
        responseDTO.setStatus(booking.getStatus().toString());
        responseDTO.setNote(booking.getNotes());
        responseDTO.setBooking_date(timeFormat.format(booking.getBooking()));
        responseDTO.setUpdated_at(timeFormat.format(booking.getUpdate()));
        return responseDTO;
    }
    public static List<bookTableDTO> fromEntity(List<TableBooking> bookings) {
        return bookings.stream()
                .map(bookTableDTO::fromEntity) // Calls the single-entity conversion method
                .collect(Collectors.toList());
    }

}