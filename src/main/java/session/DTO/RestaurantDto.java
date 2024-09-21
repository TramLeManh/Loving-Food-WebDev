package session.DTO;

import lombok.Getter;
import lombok.Setter;
import session.model.Restaurant;
import session.model.category;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class RestaurantDto {
    private Restaurant restaurant;
    private List<category> category;
    private List<Restaurant> restaurantList;
    public RestaurantDto(Restaurant restaurant, List<session.model.category> category) {
        this.restaurant = restaurant;
        this.category = category;
    }
    public RestaurantDto(List<Restaurant> restaurant, List<session.model.category> category) {
        this.restaurantList = restaurant;
        this.category = category;
    }

    public static List<RestaurantDto> fromEntity(List<Restaurant> data, List<category> categories) {
        return data.stream()
                .map(restaurant -> new RestaurantDto(restaurant, categories))
                .collect(Collectors.toList());
    }

    public static RestaurantDto fromEntity(Restaurant restaurant, List<category> categories) {
        return new RestaurantDto(restaurant, categories);
    }
}