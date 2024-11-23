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
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepo bookingRepo;
    private final BookingDecisionRepo bookingDecisionRepo;
    private List<bookTableDTO> data;

    public BookingService(BookingRepo bookingRepo, BookingDecisionRepo bookingDecisionRepo) {
        this.bookingRepo = bookingRepo;
        this.bookingDecisionRepo = bookingDecisionRepo;
    }

    /**
     * Lay thong tin nhung adminDecision cua Admin da duyet
     */
    public List<BookingDecisionResponseDTO> getAdminDecision(int owner_id, BookingStatus status) {
        List<BookingDecision> bookingDecisions = null;
        if (status == null) {
            bookingDecisions = bookingDecisionRepo.getAllBookingDecision(owner_id);
        } else {
            bookingDecisions = bookingDecisionRepo.getBookingOrder(owner_id, status);
        }
        return bookingDecisions.stream().map(BookingDecisionResponseDTO::fromEntity).collect(Collectors.toList());
    }

    /**
     * Found owner booking base on owner_id, bookingStatus
     */
    public List<bookTableDTO> getOwnerBooking(int owner_id, String bookingStatus) {
        List<TableBooking> bookings = bookingRepo.getOwnerBooking(owner_id);
        List<BookingDecision> decisions = bookingDecisionRepo.getAllBookingDecision(owner_id);
        List<bookTableDTO> data = bookTableDTO.fromEntity(bookings, decisions);
        if (bookingStatus == null) {
            return data;
        } else {
            data = data.stream().filter(booking -> Objects.equals(booking.getStatus(), bookingStatus)).collect(Collectors.toList());
        }
        return data;
    }


    public BookingDecision getDetailDecision(int decision_id) {
        if (bookingDecisionRepo.isDecisionPending(decision_id)) {
            return null;
        }
        return bookingDecisionRepo.getBookingDecisionDetailByDecisionId(decision_id);
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
    public void createDecision(int admin_user_id, int booking_id, String note, String status) {
        if (bookingDecisionRepo.isDecisionExist(booking_id)) {
            return;
        }
        int decision_id = (int) (Math.random() * 9000) + 1000;
        bookingDecisionRepo.createDecision(decision_id, admin_user_id, booking_id, note, status);
        bookingDecisionRepo.updateStatus(booking_id, status);
    }

    /**
     * Update thong tin Booking
     */
    public void updateUserBooking(int bid, String name, String phone, String date, int guests, String note) {
        bookingRepo.updateUserBooking(bid, name, phone, date, guests, note);
    }

    /**
     * Xoa user Booking
     */

    public void deleteUserBooking(int booking_id) {
        bookingRepo.deleteById(booking_id);
    }

    /**
     * Account Generate Booking
     */
    public void createUserBooking(TableBooking booking) {
        bookingRepo.createUserBooking(booking.getBookingId(), booking.getUser_id(), booking.getRestaurantId(), booking.getName(), booking.getPhoneNumber(), booking.getBookingAt(), booking.getNumOfGuests(), booking.getNotes());
    }

    /**
     * Lay thong tin booking cua user
     */
    public List<bookTableDTO> getUserBooking(int user_id, Integer bookingStatus) {
        BookingStatus status;
        try {
            status = BookingStatus.values()[bookingStatus];
        } catch (ArrayIndexOutOfBoundsException e) {
            status = null;
        }
        List<TableBooking> bookings = bookingRepo.getUserBooking(user_id);
        List<BookingDecision> decisions = bookingDecisionRepo.getUserBookingDecision(user_id);
        data = bookTableDTO.fromEntity(bookings, decisions);
        if (status == null) {
            return data;
        } else {
            data = data.stream().filter(booking -> Objects.equals(booking.getStatus(), bookingStatus)).collect(Collectors.toList());
        }
        return data;
    }

}