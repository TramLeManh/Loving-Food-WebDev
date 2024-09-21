package session.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class createAccountDTO {

    private String username;
    private String password;
    private String email;
    private int role;
}