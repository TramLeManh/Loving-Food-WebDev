package session.Dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import session.model.District;
import session.model.Restaurant;
import session.model.category;
import session.responseHandler.Exception.ServerException;

import java.util.List;
import java.util.Optional;

@Component
public class RestaurantDAO {
    private final JdbcTemplate jdbcTemplate;

    public RestaurantDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<List<Restaurant>> findAll() {
        String query = "select * FROM view_restaurant";
        return Optional.ofNullable(jdbcTemplate.query(query, new Restaurant()));
    }

    public Optional<List<Restaurant>> getByDistrict(String district) {
        try {
            String query = "select * FROM view_restaurant WHERE restaurant_district = ?";
            return Optional.ofNullable(jdbcTemplate.query(query, new Restaurant(), district));
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    public Optional<List<Restaurant>> getByCategory(String category) {
        try {
            String query = "select  view_restaurant.* from view_restaurant\n" + "where view_restaurant.restaurant_id\n" + "in (SELECT restaurant_id FROM restaurant_category where category_id = ?)";
            return Optional.ofNullable(jdbcTemplate.query(query, new Restaurant(), category));
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    public Optional<List<Restaurant>> getByCategoryDistrict(String category, String district) {
        try {
            String query = "select  * from view_restaurant where restaurant_id\n" +
                    "in (SELECT restaurant_id FROM restaurant_category where category_id = ? AND view_restaurant.restaurant_district = ?)";
            return Optional.ofNullable(jdbcTemplate.query(query, new Restaurant(), category, district));
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    public Optional<List<District>> getDistrict() {
        try {
            String query = "select * FROM district";
            return Optional.ofNullable(jdbcTemplate.query(query, new District()));
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    public Optional<Restaurant> findById(int id) {
        try {
            String query = "select view_restaurant.* from view_restaurant\n" + "where view_restaurant.restaurant_id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, new Restaurant(), id));
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    public Optional<category> getCategoryById(String categoryId) {
        try {
            String query = "SELECT * FROM category WHERE category_id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, new category(), categoryId));
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    public Optional<District> getDistrictById(String district_id) {
        try {
            String query = "select  * from district where district_id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, new District(), district_id));
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

}