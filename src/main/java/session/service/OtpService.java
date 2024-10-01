package session.service;

import org.springframework.stereotype.Component;
import session.DTO.UserDto;
import session.Dao.UserDAO;
import session.Dao.OtpDAO;
import session.Dao.createOTP;
import session.model.User;
import session.model.OTP;
import session.utils.Service.EmailService.EmailService;
import session.utils.State;
import session.utils.Status;

//This service class is used to interact with the DAO class
@Component
public class OtpService {
    private final OtpDAO db;
    private final EmailService emailService;
    private final UserDAO u;
    public OtpService(OtpDAO dao, EmailService emailService, UserDAO u) {
        this.db = dao;
        this.emailService = emailService;
        this.u = u;
    }
    //Return wheither OTP exist or not
    public OTP getOTP(String uuid) {
        return db.findById(uuid).orElse(null);
    }
    private void createOTPDatabase(OTP o) {
        db.findById(o.getUuid()).ifPresentOrElse(
                (otp) -> db.update(o),
                () -> db.save(o)
        );
    }
    /**
     * Client request sendOTP
     * Send OTP and add in table for verifying later
     */
    public Status sendOTPRecover(String uuid, int userId) {
        User user = u.findById(userId).orElse(null);
        createOTP otp = new createOTP(uuid, user.getEmail());
        if (db.findById(otp.getUuid()).isPresent()) return Status.ERROR;
        OTP o = createOTP.toEnity(otp.getUuid(), user.getEmail());
        emailService.sendOTP(user.getUsername(), user.getEmail(), o.getOtp());
        createOTPDatabase(o);
        return Status.SUCCESS;
    }
    public void sendOTPVerify(String uuid, String email) {
        OTP o = createOTP.toEnity(uuid, email);
        emailService.sendOTP(email, o.getOtp());
        createOTPDatabase(o);
    }
    /**
     * Client request email for send otp to recovery
     * Case 1: if email not found, server send invalid email
     * Case 2: if email exist , go to input OTP code page return accountDto store in session
     */
    //
    public State<UserDto> sendOTPRecovery(String uuid, String email) {
        State<UserDto> state = new State<>();
        //Nên verify email trước khi gửi OTP
        User user = u.getByEmail(email).orElse(null);
        if (user == null) {
            state.setStatus(Status.NOT_FOUND);
            return state;
        }
        OTP o = createOTP.toEnity(uuid, user.getEmail());
        createOTPDatabase(o);
        emailService.sendOTP(user.getUsername(), email, o.getOtp());
        state.setStatus(Status.SUCCESS);
        state.setData(UserDto.fromEntity(user));
        return state;
    }

    /**
     * Client request email for send otp to recovery
     *Case 1: Check OTP code outdate, tell user outdate OTP and reuqire client to send OTP again
     * Case 2: if email exist , go to input OTP code page return accountDto store in session
     */
    public State<String> verifyOTP(String uuid, int input) {
        State<String> state = new State<>();
        OTP o = db.findById(uuid).orElse(null);
        // Check if OTP record is found
        // Check if OTP is valid (not expired or out of date)
        if (db.isOTPValid(uuid).isEmpty()) {
            state.setStatus(Status.OUT_DATE);
            sendOTPRecovery(uuid, o.getEmail());
            return state;
        }
        // Check if OTP matches the input
        if (o.getOtp() != input) {
            state.setStatus(Status.ERROR);
            return state;
        }
        state.setData(o.getEmail());
        db.delete(uuid);
        state.setStatus(Status.SUCCESS);
        return state;
    }

    public boolean findSession(String uuid) {
       return db.findById(uuid).isPresent();
    }
}

//Verify OTP code from the user input and check with the database