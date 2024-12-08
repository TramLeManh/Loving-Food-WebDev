package session.Restaurant;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import session.Category.CategoryRepo;
import session.Restaurant.DTO.createRestaurantDTO;
import session.model.District;

import java.util.List;

@Component
public class RestaurantService {

    private final CategoryRepo categoryDAO;
    private final RestaurantDAO restaurantDAO;

    public RestaurantService(RestaurantDAO restaurantDAO, CategoryRepo category) {
        this.restaurantDAO = restaurantDAO;
        this.categoryDAO = category;
    }

    //Tìm list các nhà hàng theo tên và địa chỉ
    public List<Restaurant> getRestaurant(String district, String category) {
        List<Restaurant> restaurants;
        if (district == null && category == null) {
            restaurants = restaurantDAO.findAll().orElse(null);
        } else if (district != null && category == null) {
            restaurants = restaurantDAO.getByDistrict(district).orElse(null);
        } else if (district == null) {
            restaurants = restaurantDAO.getByCategory(category).orElse(null);
        } else {
            restaurants = restaurantDAO.getByCategoryDistrict(category, district).orElse(null);
        }
        if (restaurants != null) {
            restaurants.forEach(restaurant -> {
                // Set the category based on restaurant ID
                restaurant.setCategory(categoryDAO.getRestaurantCategory(restaurant.getRestaurant_id()));
            });
        }
        return restaurants;
    }

    public Restaurant getById(int id) {
        Restaurant restaurant = restaurantDAO.findById(id).orElse(null);
        // Check if restaurant is not null before setting the category
        if (restaurant != null) {
            restaurant.setCategory(categoryDAO.getRestaurantCategory(id));
        }
        return restaurant;
    }

    public List<Restaurant> getOwnerRestaurant(int owner_id,Integer restaurant_id) {
        List<Restaurant> restaurants = restaurantDAO.getOwnerRestaurant(owner_id);
        restaurants.forEach(restaurant -> {
            // Set the category based on restaurant ID
            restaurant.setCategory(categoryDAO.getRestaurantCategory(restaurant.getRestaurant_id()));
        });
        return restaurants;
    }


    public List<District> getDistrict() {
        return restaurantDAO.getDistrict().orElse(null);
    }

    //Assume that the list request is fix already
    @Transactional
    public void insertRestaurantCategories(int restaurantId, List<String> categoryIds) {
        for (String categoryId : categoryIds) {
            categoryDAO.insertCategory(restaurantId, categoryId);
        }
    }
    //This function add Category for the restaurant

    @Transactional
    public void createRestaurant(createRestaurantDTO dto,int owner_id) {
        Restaurant restaurant = dto.toEntity();
        restaurantDAO.insertRestaurant(owner_id, restaurant.getRestaurant_id(), restaurant.getName(), restaurant.getDistrict(), restaurant.getAddress(), restaurant.getDescription(), restaurant.getPicture(), restaurant.getPhone_number(), restaurant.getOpen_time(),
                restaurant.getClose_time());
        insertRestaurantCategories((restaurant.getRestaurant_id()), dto.getCategory());
    }

    public void removeRestaurant(int id) {
        restaurantDAO.removeRestaurant(id);
    }


}