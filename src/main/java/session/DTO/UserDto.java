package session.DTO;


import session.model.User;

import java.io.Serializable;


public record UserDto(int id, String username, String email, String role, String createdAt) implements Serializable {
    public static UserDto fromEntity(User save) {
        return new UserDto(save.getUser_id(), save.getUsername(), save.getEmail(), save.getCreatedAt(), save.getRole());
    }

};