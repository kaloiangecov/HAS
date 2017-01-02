package has.Meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by kaloi on 12/20/2016.
 */
@Controller
public class MealController {

    @Autowired
    private MealService mealService;

    @RequestMapping(value = "/meal", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Meal save(@RequestBody Meal meal) {
        return mealService.save(meal);
    }

    @RequestMapping(value = "/meals", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Meal> getAllMeals() {
        return mealService.getAllMeals();
    }

    @RequestMapping(value = "/meal/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Meal findMealById(@PathVariable Long id) throws Exception {
        return mealService.findById(id);
    }

    @RequestMapping(value = "/meal/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Meal removeMealById(@PathVariable Long id) throws Exception {
        return mealService.remove(id);
    }

    @RequestMapping(value = "/meal/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Meal updateMeal(@PathVariable Long id, @RequestBody Meal meal) throws Exception {
        return mealService.update(id, meal);
    }
}
