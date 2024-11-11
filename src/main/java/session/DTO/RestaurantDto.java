package session.DTO;

import lombok.Getter;
import lombok.Setter;
import session.Restaurant.Restaurant;
import session.Category.Category;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class RestaurantDto {
    private Restaurant restaurant;
    private List<Category> category;
    private List<Restaurant> restaurantList;
    public RestaurantDto(Restaurant restaurant, List<Category> category) {
        this.restaurant = restaurant;
        this.category = category;
    }
    public RestaurantDto(List<Restaurant> restaurant, List<Category> category) {
        this.restaurantList = restaurant;
        this.category = category;
    }

    public static List<RestaurantDto> fromEntity(List<Restaurant> data, List<Category> categories) {
        return data.stream()
                .map(restaurant -> new RestaurantDto(restaurant, categories))
                .collect(Collectors.toList());
    }

    public static RestaurantDto fromEntity(Restaurant restaurant, List<Category> categories) {
        return new RestaurantDto(restaurant, categories);
    }
}