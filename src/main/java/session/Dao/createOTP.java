package session.Dao;

import lombok.Getter;
import lombok.Setter;
import session.model.OTP;
@Getter
@Setter
public class createOTP {
    private final String uuid;
    private final String email;

    public createOTP(String uuid, String email) {
        this.uuid = uuid;
        this.email = email;
    }

    public static OTP toEnity(String uuid, String email){
        OTP otp = new OTP();
        otp.setUuid(uuid);
        otp.setEmail(email);
        otp.setOtp((int) (Math.random() * 9000) + 1000);
        return otp;
    }
}