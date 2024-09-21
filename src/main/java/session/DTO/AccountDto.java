package session.DTO;


import session.model.Account;

import java.io.Serializable;


public record AccountDto(int id, String username, String email, String role, String createdAt) implements Serializable {
    public static AccountDto fromEntity(Account save) {
        return new AccountDto(save.getUser_id(), save.getUsername(), save.getEmail(), save.getCreatedAt(), save.getRole());
    }

};