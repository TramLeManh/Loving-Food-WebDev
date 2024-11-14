package session.Booking.DTO;

import lombok.Getter;
import lombok.Setter;
import session.Booking.Model.TableBooking;

@Getter
@Setter
public class CreateBookTableDTO {
    private int BookingId;
    private String user_id;
    private int restaurant_id;
    private String name;
    private String phone;
    private String time;
    private int number_of_guests;
    private String note;
    public static TableBooking toEntity(CreateBookTableDTO createBookTableDTO) {
        TableBooking tableBooking = new TableBooking();
        tableBooking.setBookingId((int) (Math.random() * 9000) + 1000);
        tableBooking.setUser_id(createBookTableDTO.getUser_id());
        tableBooking.setRestaurantId(createBookTableDTO.getRestaurant_id());
        tableBooking.setName(createBookTableDTO.getName());
        tableBooking.setPhoneNumber(createBookTableDTO.getPhone());
        tableBooking.setBookingAt(createBookTableDTO.getTime());
        tableBooking.setNumOfGuests(createBookTableDTO.getNumber_of_guests());
        tableBooking.setNotes(createBookTableDTO.getNote());
        return tableBooking;
    }
}