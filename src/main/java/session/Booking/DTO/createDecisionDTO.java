package session.Booking.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class createDecisionDTO {
    private int booking_id;
    private int owner_id;
    private String status;
    private String note;
}