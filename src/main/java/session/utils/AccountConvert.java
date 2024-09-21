package session.utils;

import session.DTO.createAccountDTO;
import session.model.Account;

public class AccountConvert {
    public static Account toEntity(createAccountDTO model) throws Exception {
        Account account = new Account();
        int code = (int) (Math.random() * 9000) + 1000;
        account.setUser_id(code);
        account.setUsername(model.getUsername());
        account.setPassword(PasswordEncryptor.encryptPassword(model.getPassword()));
        account.setEmail(model.getEmail());
        account.setRole(role(model.getRole()));
        return account;
    }

    public static String role(int role_id) {
        if (role_id == 1) {
            return "ADMIN";
        }
        return "USER";
    }

}