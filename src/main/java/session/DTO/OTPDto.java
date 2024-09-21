package session.DTO;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public  class OTPDto {
    int user_id;
    int otp;
    public OTPDto(int user_id, int otp) {
        this.user_id = user_id;
        this.otp = otp;
    }
}