package session.service;

import org.springframework.stereotype.Component;
import session.DTO.AccountDto;
import session.Dao.AccountDAO;
import session.Dao.OtpDAO;
import session.model.Account;
import session.model.OTP;
import session.utils.Service.EmailService.EmailService;
import session.utils.State;
import session.utils.Status;

import java.util.Optional;

//This service class is used to interact with the DAO class
@Component
public class OtpService {
    private final OtpDAO db;
    private final EmailService emailService;
    private final AccountDAO u;
    public OtpService(OtpDAO dao, EmailService emailService, AccountDAO u) {
        this.db = dao;
        this.emailService = emailService;
        this.u = u;
    }
    //Return wheither OTP exist or not
    public Optional<OTP> getOTP(int user_id) {
        return db.findById(user_id);
    }
    public void createOTPDatabase(OTP o) {
        this.getOTP(o.getUser_id()).ifPresentOrElse((otp) -> db.update(o), () -> db.save(o));
    }

    /**
     * Client request sendOTP
     * Send OTP and add in table for verifying later
     */
    public Status sendOTP(int user_id) {
        Account account = u.findById(user_id).orElse(null);
        if (db.findById(user_id).isPresent()) return Status.ERROR;
        int code = (int) (Math.random() * 9000) + 1000;
        OTP otp = new OTP(user_id, code);
        emailService.sendOTP(account.getUsername(), account.getEmail(), code);
        createOTPDatabase(otp);
        return Status.SUCCESS;
    }

    /**
     * Client request resendOTP
     * Case 1: if OTP not found, server resend the OTP
     * Case 2: if OTP found in table and still valid <10 minutes, cleint recheck their email again
     * Case 3:
     */
    public Status resendOTP(int user_id) {
        if (u.findById(user_id).isEmpty()) {
            sendOTP(user_id);
            return Status.NOT_FOUND;
        }
        ;
        if (db.isOTPValid(user_id).isPresent()) return Status.ERROR;// If otp <10 minutes, return error
        sendOTP(user_id);
        return Status.SUCCESS;
    }

    /**
     * Client request email for send otp to recovery
     * Case 1: if email not found, server send invalid email
     * Case 2: if email exist , go to input OTP code page return accountDto store in session
     */
    public State<AccountDto> sendOTPRecovery(String email) {
        State<AccountDto> state = new State<>();
        Account account = u.getByEmail(email).orElse(null);
        if (account == null) {
            state.setStatus(Status.NOT_FOUND);
            return state;
        }
        int code = (int) (Math.random() * 9000) + 1000;
        OTP otp = new OTP(account.getUser_id(), code);
        createOTPDatabase(otp);
        emailService.sendOTP(account.getUsername(), email, code);
        state.setStatus(Status.SUCCESS);
        state.setData(AccountDto.fromEntity(account));
        return state;
    }

    /**
     * Client request email for send otp to recovery
     *Case 1: Check OTP code outdate, tell user outdate OTP and reuqire client to send OTP again
     * Case 2: if email exist , go to input OTP code page return accountDto store in session
     */
    public Status verifyOTP(int user_id, int input) {
        OTP o = db.findById(user_id).orElse(null);
        if (o == null) return Status.NOT_FOUND;
        if (db.isOTPValid(user_id).isEmpty()) return Status.OUT_DATE;
        if (o.getOtp() != input) return Status.ERROR;
        return Status.SUCCESS;
    }
}

//Verify OTP code from the user input and check with the database