package session.utils;

import session.DTO.createAccountDTO;
import session.model.User;

public class AccountConvert {
    public static User toEntity(createAccountDTO model) throws Exception {
        User user = new User();
        int code = (int) (Math.random() * 9000) + 1000;
        user.setUser_id(code);
        user.setUsername(model.getUsername());
        user.setPassword(PasswordEncryptor.encryptPassword(model.getPassword()));
        user.setEmail(model.getEmail());
        user.setRole(role(model.getRole()));
        return user;
    }

    public static String role(int role_id) {
        if (role_id == 1) {
            return "ADMIN";
        }
        return "USER";
    }

}