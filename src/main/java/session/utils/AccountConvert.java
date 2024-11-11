package session.utils;

import session.User.User;
import session.User.DTO.createUserDTO;

public class AccountConvert {
    public static User toEntity(createUserDTO model) throws Exception {
        User user = new User();
        int code = (int) (Math.random() * 9000) + 1000;
        user.setUser_id(code);
        user.setUsername(model.getUsername());
        user.setPassword(PasswordEncryptor.encryptPassword(model.getPassword()));
        user.setEmail(model.getEmail());
        user.setRole(model.getRole());
        return user;
    }



}