package session.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import session.model.category;
import session.service.RestaurantService;

import java.util.List;

//Get category
@RestController
public class categoryController {
    private final RestaurantService res;
    public categoryController(RestaurantService restaurantService) {
        this.res = restaurantService;
    }
    @RequestMapping("/category")
    public List<category> getCategory() {
        return res.getAllCategory();
    }
    @RequestMapping("/category/{category_id}")
    public category getCategory(@PathVariable String category_id) {
        return res.getCategoryById(category_id);
    }
}