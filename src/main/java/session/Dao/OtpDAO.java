package session.Dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import session.model.OTP;
import session.utils.DAO;

import java.util.List;
import java.util.Optional;
@Component
public class OtpDAO implements DAO<OTP, Integer> {
    private final JdbcTemplate jdbcTemplate;

    public OtpDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Kiểm tra OTP có trong bảng
     */
    @Override
    public Optional<OTP> findById(Integer id) {
        try {
            String query = "select * FROM OTP WHERE user_id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, new OTP(), id));
        }catch(Exception e){
            return Optional.empty();
        }
    }
    /**
     * Check wheither OTP is exist
     *  OTP tồn tại 10 minues. After 10 minutes, OTP delete
     */
    public Optional<OTP> isOTPValid(int user_id) {
        try {
            String query = "SELECT * FROM otp\n" + "WHERE created_at >= (NOW() - INTERVAL 10 MINUTE) AND user_id = ?;";
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, new OTP(), user_id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<OTP>> findAll() {
        return Optional.empty();
    }
    /**
     *
     *  Delete OTP after verify success
     */
    @Override
    public void delete(Integer entity) {
        String query = "DELETE FROM OTP WHERE user_id = ?";
        jdbcTemplate.update(query, entity);
    }
    /**
     *
     *  Update OTP
     */


    @Override
    public void update(OTP entity) {
        String query = "UPDATE OTP SET otp = ? WHERE user_id = ?";
        jdbcTemplate.update(query,entity.getOtp(), entity.getUser_id());


    }

    /**
     *
     *  Create OTP
     */
    @Override
    public void save(OTP entity) {
        String query = "INSERT INTO OTP (user_id, otp) VALUES (?, ?)";
        jdbcTemplate.update(query,entity.getUser_id(),entity.getOtp());
    }





}