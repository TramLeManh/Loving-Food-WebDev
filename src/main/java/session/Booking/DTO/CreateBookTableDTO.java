package session.Booking.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBookTableDTO {
    private int bookingId = (int) (Math.random() * 9000) + 1000;
    private int user_id;
    private int restaurant_id;
    private String name;
    private String phone;
    private String time;
    private int number_of_guests;
    private String note;
}