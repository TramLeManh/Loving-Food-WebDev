package session.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Getter
@Setter

public class Restaurant implements RowMapper<Restaurant> {

    private int restaurant_id;
    private String name;
    private String address;
    private String district;
    private String picture;
    private String open_time;
    private String close_time;
    private String phone_number;
    private List<String> category;

    @Override
    public Restaurant mapRow(ResultSet rs, int rowNum) throws SQLException {
        Restaurant res = new Restaurant();
        res.setRestaurant_id(rs.getInt("restaurant_id"));
        res.setName(rs.getString("restaurant_name"));
        res.setAddress(rs.getString("restaurant_address"));
        res.setDistrict(rs.getString("restaurant_district"));
        res.setPicture(rs.getString("restaurant_image"));
        res.setOpen_time(rs.getString("open_time"));
        res.setClose_time(rs.getString("close_time"));
        res.setPhone_number(rs.getString("phone_number"));
        return res;
    }
}