package session.service;

import org.springframework.stereotype.Component;
import session.DTO.AccountDto;
import session.DTO.createAccountDTO;
import session.Dao.AccountDAO;
import session.model.Account;
import session.utils.AccountConvert;
import session.utils.PasswordEncryptor;
import session.utils.State;
import session.utils.Status;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class AccountService {
    private final AccountDAO acc;
    public AccountService(AccountDAO dao) {
        this.acc = dao;
    }
    public List<AccountDto> getAllAccount() {
        return acc.findAll().map(accounts -> accounts.stream().map(AccountDto::fromEntity).collect(Collectors.toList())).orElse(Collections.emptyList());
    }
    public AccountDto findUser(int id) {
        return acc.findById(id).map(AccountDto::fromEntity).orElse(null);
    }

    public State<AccountDto> createAccount(createAccountDTO accountDto) throws Exception {

        try {
            Account account = AccountConvert.toEntity(accountDto);
            State<AccountDto> state = new State<>(AccountDto.fromEntity(account));
            if (acc.getByUsername(account.getUsername()).isPresent()) state.setStatus(Status.EXIST_USERNAME);
            else if (acc.getByEmail(account.getEmail()).isPresent()) state.setStatus(Status.EXIST_EMAIL);
            else {
                acc.save(account);
                state.setStatus(Status.SUCCESS);
            }
            ;
            return state;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    public State<AccountDto> login(String user_name, String password) {
        State<AccountDto> state = new State<AccountDto>();
        if (acc.getByUsername(user_name).isEmpty()) state.setStatus(Status.NOT_FOUND);
        else {
            Account account = acc.getByUsername(user_name).get();
            System.out.println(account.getPassword());
            if (PasswordEncryptor.verifyPassword(password, account.getPassword())) {
                state.setStatus(Status.SUCCESS);
                state.setData(AccountDto.fromEntity(account));
            } else state.setStatus(Status.UNAUTHORIZED);
        }
        return state;
    }


}