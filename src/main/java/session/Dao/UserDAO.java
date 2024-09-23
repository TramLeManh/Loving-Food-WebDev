package session.Dao;
//https://www.youtube.com/watch?v=0uLqdBpYAVA

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import session.model.User;
import session.model.userInformation;
import session.utils.DAO;

import java.util.List;
import java.util.Optional;

/**
 * UserDAO
 */
@Component
public class UserDAO implements DAO<User, Integer> {
    private final JdbcTemplate jdbcTemplate;

    public UserDAO(JdbcTemplate j) {
        this.jdbcTemplate = j;
    }

    public Optional<User> findById(int id) {
        String query = "select * FROM User WHERE user_id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(query, new User(), id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.empty();
    }
    @Override
    public Optional<List<User>> findAll() {
        String query = "select * FROM view_account";
        return Optional.of(jdbcTemplate.query(query, new User()));
    }
    @Override
    public void save(User entity) {
        String query = "INSERT INTO User (user_id, username, user_password, user_email,user_role) VALUES (?, ?, ?, ?,?)";
        jdbcTemplate.update(query, entity.getUser_id(), entity.getUsername(), entity.getPassword(), entity.getEmail(), entity.getRole());
    }
    @Override
    public void update(User entity) {
        String query = "UPDATE User SET username = ?, user_email = ?, user_password = ? WHERE user_id = ?";
        jdbcTemplate.update(query, entity.getUsername(), entity.getEmail(), entity.getPassword(), entity.getUser_id());
    }
    @Override
    public void delete(Integer entity) {
        String query = "DELETE FROM User WHERE user_id = ?";
        jdbcTemplate.update(query, entity);
    }
    public Optional<User> getByEmail(String email) {
        String query = "select * FROM user WHERE user_email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, new User(), email));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    public Optional<User> getByUsername(String username) {
        String query = "select * FROM user WHERE username = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, new User(), username));
        } catch (Exception e) {

            return Optional.empty();
        }
    }
    public void updatePassword(int user_id, String password) {
        String query = "UPDATE user SET user_password = ? WHERE user_id = ?";
        jdbcTemplate.update(query, password, user_id);
    }
    public void createInformation(userInformation entity) {
        String query = "INSERT INTO user_information (user_id, full_name, phone_number, address,profile_picture_url) VALUES (?, ?, ?, ?,?)";
        jdbcTemplate.update(query, entity.getUser_id(), entity.getFull_name(), entity.getPhone_number(), entity.getAddress(), entity.getProfile_picture_url());
    }
    public void updateInformation(userInformation entity) {
        String query = "UPDATE user_information  SET full_name = ?, phone_number = ?, address = ?, profile_picture_url = ? WHERE user_id = ?";
        jdbcTemplate.update(query, entity.getFull_name(), entity.getPhone_number(), entity.getAddress(), entity.getProfile_picture_url(),entity.getUser_id());
    }
    public userInformation getInformation(int id) {
        String query = "select * FROM user_information WHERE user_id = ?";
        return jdbcTemplate.queryForObject(query, new userInformation(), id);
    }

}