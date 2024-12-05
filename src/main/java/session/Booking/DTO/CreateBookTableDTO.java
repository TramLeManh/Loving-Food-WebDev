package session.Booking.DTO;

import lombok.Getter;
import lombok.Setter;
import session.Booking.Model.TableBooking;

@Getter
@Setter
public class CreateBookTableDTO {
    private int booking_id;//hide this param
    private  int restaurant_id;
    private String name;
    private String phone;
    private String time;
    private int number_of_guests;
    private String note;
    public static TableBooking toEntity(CreateBookTableDTO createBookTableDTO, String user_id) {
        TableBooking tableBooking = new TableBooking();
        tableBooking.setBookingId(createBookTableDTO.getBooking_id());
        tableBooking.setUser_id(user_id);
        tableBooking.setRestaurantId(createBookTableDTO.getRestaurant_id());
        tableBooking.setCustomer_name(createBookTableDTO.getName());
        tableBooking.setPhoneNumber(createBookTableDTO.getPhone());
        tableBooking.setBookingAt(createBookTableDTO.getTime());
        tableBooking.setNumOfGuests(createBookTableDTO.getNumber_of_guests());
        tableBooking.setNotes(createBookTableDTO.getNote());
        return tableBooking;
    }


}