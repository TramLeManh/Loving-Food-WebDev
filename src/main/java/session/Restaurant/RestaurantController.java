package session.Restaurant;

import jakarta.servlet.http.HttpSession;
import org.springframework.data.repository.Repository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import session.Restaurant.DTO.RestaurantResponseDto;
import session.Restaurant.DTO.addCategoryDTO;
import session.Restaurant.DTO.createRestaurantDTO;
import session.model.District;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/{id}")
    public Restaurant getRestaurant(@PathVariable int id) {
        return restaurantService.getById(id);
    }

    //Tìm Nhà hàng query theo Category và district
    @GetMapping("/get")
    public List<RestaurantResponseDto> findRestaurant(@RequestParam(value = "district", required = false) String district, @RequestParam(value = "category", required = false) String category) {
        List<Restaurant> data = restaurantService.getRestaurant(district, category);
        return Collections.singletonList(new RestaurantResponseDto(data));
    }

    @GetMapping("/getDistrict")
    public List<District> getD() {
        return restaurantService.getDistrict();
    }
    @GetMapping("/getOwnerRestaurant")
    public List<Restaurant> getOwnerRestaurant(HttpSession session, @RequestParam(value = "owner_id", required = false) String owner_id) {
        String user_id = (String) session.getAttribute("user_id");
        if(user_id == null) {
            return restaurantService.getOwnerRestaurant(Integer.parseInt(owner_id));
        }
        return restaurantService.getOwnerRestaurant(Integer.parseInt(user_id));
    }

    @PostMapping("/insertCategory")
    public ResponseEntity<Map<String, Object>> insertCategory(@RequestBody addCategoryDTO addCategoryDTO) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            response.put("message", "Insert category successfully");
            restaurantService.insertRestaurantCategories(addCategoryDTO.getRestaurant_id(), addCategoryDTO.getCategory_id());
            return ResponseEntity.ok().body(response);
        }catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

    }
    @PostMapping("/insertRestaurant")
    public ResponseEntity<Map<String, Object>> insertRestaurant(HttpSession session, @RequestBody createRestaurantDTO createRestaurantDTO) {
        String user_id = (String) session.getAttribute("user_id");
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            response.put("message", "Insert restaurant successfully");
            restaurantService.createRestaurant(createRestaurantDTO);
            return ResponseEntity.ok().body(response);
        }catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

    }
}