package session.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter


public class OTP implements RowMapper<OTP> {
    private String uuid;
    private  int user_id;
    private  int otp;
    public OTP(int user_id, int otp) {
        this.user_id = user_id;
        this.otp = otp;
    }
    public OTP() {
    }

    //Map row where map the table result set to the object
    @Override
    public OTP mapRow(ResultSet rs, int rowNum) throws SQLException {
        OTP otp = new OTP();
        otp.setUser_id(rs.getInt("user_id"));
        otp.setUuid(rs.getString("uuid"));
        otp.setOtp(rs.getInt("otp"));
        return otp;
    }
}