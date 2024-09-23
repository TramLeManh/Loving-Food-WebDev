package session.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
@Getter
@Setter
public class userInformation implements RowMapper<userInformation> {
    private int user_id;
    private String full_name;
    private String phone_number;
    private  String address;
    private String profile_picture_url;
    public userInformation(){};

    @Override
    public userInformation mapRow(ResultSet rs, int rowNum) throws SQLException {
        userInformation user = new userInformation();
        user.setUser_id(rs.getInt("user_id"));
        user.setFull_name(rs.getString("full_name"));
        user.setPhone_number(rs.getString("phone_number"));
        user.setAddress(rs.getString("address"));
        user.setProfile_picture_url(rs.getString("profile_picture_url"));
        return user;
    }
}