package session.Booking;

import org.springframework.stereotype.Service;
import session.Booking.DAO.BookingDecisionRepo;
import session.Booking.DAO.BookingRepo;
import session.Booking.DTO.BookingDecisionResponseDTO;
import session.Booking.DTO.bookTableDTO;
import session.Booking.Model.BookingDecision;
import session.Booking.Model.TableBooking;
import session.utils.Enum.BookingStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepo bookingRepo;
    private final BookingDecisionRepo bookingDecisionRepo;

    public BookingService(
            BookingRepo bookingRepo,
            BookingDecisionRepo bookingDecisionRepo
    ) {
        this.bookingRepo = bookingRepo;
        this.bookingDecisionRepo = bookingDecisionRepo;
    }

    public List<TableBooking> getAllBooking() {
        return bookingRepo.findAll();
    }

    /**
     * Lay thong tin nhung decision cua Admin da duyet
     */
    public List<BookingDecisionResponseDTO> getAdminDecision(
            int owner_id,
            BookingStatus status
    ) {
        List<BookingDecision> bookingDecisions = null;
        if (status == null) {
            bookingDecisions = bookingDecisionRepo.viewAllBookingDecision(owner_id);
        } else {
            bookingDecisions = bookingDecisionRepo.getBookingOrder(owner_id, status);
        }
        return bookingDecisions
                .stream()
                .map(BookingDecisionResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Lay thong tin cua booking khach hang dat
     */
    public List<bookTableDTO> getIncomeBooking(int owner_id) {
        return bookTableDTO.fromEntity(bookingRepo.getOwnerBooking(owner_id));
    }

    /**
     * Admin duyet thong tin
     */

    public void updateDecision(int booking_id, String status, String note) {
        if (status != null) {
            bookingDecisionRepo.updateStatus(booking_id, status);
        }
        // Process note update if note is provided
        if (note != null) {
            bookingDecisionRepo.updateNotes(booking_id, note);
        }
    }

    /**
     * Kiem tra admin da dua ra decesion hay chua
     */
    public boolean isDecisionExist(int booking_id) {
        return bookingDecisionRepo.isDecisionExist(booking_id);
    }

    /**
     * Kiem tra admin da phan hoi booking chua
     */

    public boolean isDecisionPending(int booking_id) {
        return bookingDecisionRepo.isDecisionPending(booking_id);
    }

    /**
     * Admin dua ra decison cho booking
     */
    public void createDecision(
            int admin_user_id,
            int booking_id,
            String note,
            String status
    ) {
        if (bookingDecisionRepo.isDecisionExist(booking_id)) {
            return;
        }
        int decision_id = (int) (Math.random() * 9000) + 1000;
        bookingDecisionRepo.createDecision(
                decision_id,
                admin_user_id,
                booking_id,
                note,
                status
        );
        bookingDecisionRepo.updateStatus(booking_id, status);
    }

    /**
     * Update thong tin Booking
     */
    public boolean updateUserBooking(
            int bid,
            String name,
            String phone,
            String date,
            int guests,
            String note
    ) {
        bookingRepo.updateUserBooking(bid, name, phone, date, guests, note);
        return true;
    }

    /**
     * Xoa user Booking
     */

    public boolean deleteUserBooking(int booking_id) {
        bookingRepo.deleteById(booking_id);
        return true;
    }

    /**
     * User Generate Booking
     */
    public boolean createUserBooking(
            int bid,
            int user,
            int restaurant,
            String customer,
            String phone,
            String time,
            int guests,
            String note
    ) {
        bookingRepo.createUserBooking(
                bid,
                user,
                restaurant,
                customer,
                phone,
                time,
                guests,
                note
        );
        return true;
    }

    /**
     * Get nhung booking
     */
    public List<TableBooking> getUserBooking(int user_id, String bookingStatus) {
        List<TableBooking> bookings = null;
        if (bookingStatus == null) {
            bookings = bookingRepo.getUserBooking(user_id);
        } else {
            //Due to native query, we need to pass the status as a string
            bookings = bookingRepo.getUserBookingByStatus(user_id, bookingStatus.toString());
        }
        return bookings;
    }

    public BookingDecision getUserDetailDecision(int booking_id) {
        return bookingDecisionRepo.viewBookingDecisionDetailByBookingId(booking_id);
    }

    public BookingDecision getOwnerDetailDecision(int decision_id) {
        return bookingDecisionRepo.viewBookingDecisionDetailByDecisionId(decision_id);
    }
    public TableBooking getBookingDetail(int booking_id) {
        return bookingRepo.findById(booking_id).orElse(null);
    }
}