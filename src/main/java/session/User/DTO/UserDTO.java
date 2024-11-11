package session.User.DTO;


import java.io.Serializable;

import session.User.User;
import session.utils.PasswordEncryptor;


public record UserDTO(int id, String username, String email, String role) implements Serializable {
    public static UserDTO fromEntity(User save) {
        return new UserDTO(save.getUser_id(), save.getUsername(), save.getEmail(), save.getCreatedAt());
    }
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
};