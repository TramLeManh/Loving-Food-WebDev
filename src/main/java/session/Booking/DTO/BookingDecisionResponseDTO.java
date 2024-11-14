package session.Booking.DTO;

import lombok.Getter;
import lombok.Setter;
import session.Booking.Model.BookingDecision;
import session.utils.Enum.BookingStatus;
import session.utils.timeFormat;

@Getter
@Setter
public class BookingDecisionResponseDTO {
    private long decision_id;
    private int bookingId;
    private BookingStatus status;
    private String adminNote;
    private String decisionDate;

    public static BookingDecisionResponseDTO fromEntity(BookingDecision booking){
        BookingDecisionResponseDTO responseDTO = new BookingDecisionResponseDTO();
        responseDTO.decision_id = booking.getDecision_id();
        responseDTO.bookingId = booking.getBookingInformation().getBookingId();
        responseDTO.status = booking.getStatus();
        responseDTO.adminNote = booking.getAdminNote();
        responseDTO.decisionDate = timeFormat.format(booking.getDate());

        return responseDTO;
    }
}