package session.service;

import org.springframework.stereotype.Component;
import session.DTO.UserDto;
import session.DTO.createAccountDTO;
import session.DTO.userInfomationDTO;
import session.Dao.UserDAO;
import session.model.User;
import session.model.userInformation;
import session.utils.AccountConvert;
import session.utils.PasswordEncryptor;
import session.utils.State;
import session.utils.Status;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class AccountService {
    private final UserDAO acc;
    public AccountService(UserDAO dao) {
        this.acc = dao;
    }
    public List<UserDto> getAllAccount() {
        return acc.findAll().map(accounts -> accounts.stream().map(UserDto::fromEntity).collect(Collectors.toList())).orElse(Collections.emptyList());
    }
    public UserDto findUser(int id) {
        return acc.findById(id).map(UserDto::fromEntity).orElse(null);
    }

    public State<UserDto> createAccount(createAccountDTO accountDto) throws Exception {

        try {
            User user = AccountConvert.toEntity(accountDto);
            State<UserDto> state = new State<>(UserDto.fromEntity(user));
            if (acc.getByUsername(user.getUsername()).isPresent()) state.setStatus(Status.EXIST_USERNAME);
            else if (acc.getByEmail(user.getEmail()).isPresent()) state.setStatus(Status.EXIST_EMAIL);
            else {
                acc.save(user);
                state.setStatus(Status.SUCCESS);
            }
            ;
            return state;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }


    public State<UserDto> login(String user_name, String password) {
        State<UserDto> state = new State<UserDto>();
        if (acc.getByUsername(user_name).isEmpty()) state.setStatus(Status.NOT_FOUND);
        else {
            User user = acc.getByUsername(user_name).get();;
            if (PasswordEncryptor.verifyPassword(password, user.getPassword())) {
                state.setStatus(Status.SUCCESS);
                state.setData(UserDto.fromEntity(user));
            } else state.setStatus(Status.UNAUTHORIZED);
        }
        return state;
    }
    public void createInformation(userInfomationDTO user) {
          acc.createInformation(userInfomationDTO.toEntity(user));
    }
    public userInfomationDTO getInformation(int id) {
        return userInfomationDTO.fromEntity(acc.getInformation(id));
    }
    public void update(userInfomationDTO user) {
        acc.updateInformation(userInfomationDTO.toEntity(user));
    }
}