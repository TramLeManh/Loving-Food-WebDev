package session.utils.Service;

import org.springframework.stereotype.Component;
import session.Dao.AccountDAO;
import session.utils.PasswordEncryptor;
import session.utils.Service.EmailService.EmailService;
import session.utils.Status;

@Component
public class PasswordService {
    private final AccountDAO u;

    public PasswordService(AccountDAO u, EmailService emailService) {
        this.u = u;
    }

    //Account must be validate before update password
    public Status updatePassword(int user_id, String input) {
        return u.findById(user_id).map(account -> {
            String encrypt = null;
            try {
                encrypt = PasswordEncryptor.encryptPassword(input);
                account.setPassword(encrypt);
                u.update(account);
                return Status.SUCCESS;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).orElse(Status.ACCOUNT_NOT_FOUND);
    }

    //Get password from database
    public String getPassword(int user_id) {
        return u.findById(user_id).map(account -> {
            String dencrypt = null;
            try {
                return dencrypt = PasswordEncryptor.decryptPassword(account.getPassword());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).orElse(" ");
    }

    //Check wheither password is correct
    public boolean validPassword(int user_id, String input) {
        return u.findById(user_id).map(account -> {
            try {
                return input.equals(PasswordEncryptor.decryptPassword(account.getPassword()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).orElse(false);
    }
}