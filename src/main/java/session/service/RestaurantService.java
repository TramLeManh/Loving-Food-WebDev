package session.service;

import org.springframework.stereotype.Component;
import session.Dao.RestaurantDAO;
import session.Dao.categoryDAO;
import session.model.District;
import session.model.Restaurant;
import session.model.category;

import java.util.List;

@Component
public class RestaurantService {
    private final RestaurantDAO res;
    private final categoryDAO category;

    public RestaurantService(RestaurantDAO restaurantDAO, categoryDAO category) {
        this.res = restaurantDAO;
        this.category = category;
    }
    private List<Restaurant> findByDistrict(String district) {
        return res.getByDistrict(district).orElse(null);
    }
    public category getCategoryById(String category_id) {
        return res.getCategoryById(category_id).orElse(null);
    }
    public List<category> getAllCategory() {
        return category.getAllCategory().orElse(null);
    }
    //Tìm list các nhà hàng theo tên và địa chỉ
    public List<Restaurant> getRestaurant(String district, String category) {
        List<Restaurant> restaurants;
        if (district == null && category == null) {
            restaurants = res.findAll().orElse(null);
        } else if (district != null && category == null) {
            restaurants = findByDistrict(district);
        } else if (district == null) {
            restaurants = res.getByCategory(category).orElse(null);
        } else {
            restaurants = res.getByCategoryDistrict(category, district).orElse(null);
        }
        return addCategory(restaurants);
    }
    public List<District> getDistrict() {
        return res.getDistrict().orElse(null);
    }
    public Restaurant findById(int id) {
        return addCategory(res.findById(id).orElse(null));
    }
    private List<Restaurant> addCategory(List<Restaurant> restaurants) {
        if (restaurants == null) {
            return null;
        }
        for (Restaurant restaurant : restaurants) {
            List<String> categories = category.getRestaurantCategory(restaurant.getRestaurant_id()).orElse(null);
            restaurant.setCategory(categories);
        }
        return restaurants;
    }
    //This function add category for the restaurant
    private Restaurant addCategory(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }
        List<String> categories = category.getRestaurantCategory(restaurant.getRestaurant_id()).orElse(null);
        restaurant.setCategory(categories);
        return restaurant;
    }
}