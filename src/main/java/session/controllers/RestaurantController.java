package session.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import session.DTO.RestaurantResponseDto;
import session.model.District;
import session.model.Restaurant;
import session.service.RestaurantService;

import java.util.Collections;
import java.util.List;
@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
    @RequestMapping("/{id}")
    public Restaurant getRestaurant(@PathVariable int id) {
        return restaurantService.findById(id);
    }

    //Tìm Nhà hàng query theo category và district
    @RequestMapping("/get")
    public List<RestaurantResponseDto> findRestaurant(@RequestParam(value = "district", required = false) String district, @RequestParam(value = "category", required = false) String category) {
        List<Restaurant> data = restaurantService.getRestaurant(district, category);
        return Collections.singletonList(new RestaurantResponseDto(data));
    }

    @RequestMapping("/getDist")
    public List<District> getD() {
        List<District> data = restaurantService.getDistrict();
        return data;
    }
}