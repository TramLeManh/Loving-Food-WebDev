package session.DTO;

import lombok.Getter;
import lombok.Setter;
import session.model.Restaurant;

import java.util.List;

@Getter
@Setter
public class RestaurantResponseDto {
    private int count;
    private List<Restaurant> data;

    public RestaurantResponseDto(List<Restaurant> res) {
        this.count = res.size();
        this.data = res;
    }

}