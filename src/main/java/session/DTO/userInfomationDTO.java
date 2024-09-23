package session.DTO;

import lombok.Getter;
import lombok.Setter;
import session.model.User;
import session.model.userInformation;

@Getter
@Setter

public class userInfomationDTO {
    private String full_name;
    private String phone_number;
    private  String address;
    private String profile_picture_url;
    public static userInformation toEntity(userInfomationDTO dto){
        userInformation user = new userInformation();
        user.setFull_name(dto.getFull_name());
        user.setPhone_number(dto.getPhone_number());
        user.setAddress(dto.getAddress());
        user.setProfile_picture_url(dto.getProfile_picture_url());
        return user;
    }
    public static userInfomationDTO fromEntity(userInformation user){
        userInfomationDTO dto = new userInfomationDTO();
        dto.setFull_name(user.getFull_name());
        dto.setPhone_number(user.getPhone_number());
        dto.setAddress(user.getAddress());
        dto.setProfile_picture_url(user.getProfile_picture_url());
        return dto;
    }
}