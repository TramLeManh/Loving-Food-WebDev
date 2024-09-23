package session.Dao;

import lombok.Getter;
import lombok.Setter;
import session.model.OTP;
@Getter
@Setter
public class createOTP {
    private final String uuid;
    private final int user_id;

    public createOTP(String uuid, int userId) {
        this.uuid = uuid;
        user_id = userId;
    }

    public static OTP toEnity(String uuid, int user_id){
        OTP otp = new OTP();
        otp.setUuid(uuid);
        otp.setUser_id(user_id);
        otp.setOtp((int) (Math.random() * 9000) + 1000);
        return otp;
    }
}