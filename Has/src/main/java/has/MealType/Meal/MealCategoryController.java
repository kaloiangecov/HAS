package has.MealType.Meal;

import has.Meal.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by gundev on 19/02/2017.
 */
@RestController
public class MealCategoryController {

    @Autowired
    private MealCategoryService mealCategoryService;

    @RequestMapping(value = "/mealTypes", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_CREATE_MEAL')")
    public MealCategory save(@RequestBody @Valid MealCategory mealCategory) {
        return mealCategoryService.save(mealCategory);
    }

    @RequestMapping(value = "/mealTypes/all", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_MEAL')")
    public List<MealCategory> getAllMealCategories() {
        return mealCategoryService.getAllMealCategories();
    }

    @RequestMapping(value = "/mealTypes/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_MEAL')")
    public MealCategory findMealById(@PathVariable Long id) throws Exception {
        return mealCategoryService.findById(id);
    }

    @RequestMapping(value = "/mealTypes/meals/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_MEAL')")
    public List<Meal> findMealsByCategoryId(@PathVariable Long id) throws Exception {
        return mealCategoryService.findMealsByCategoryId(id);
    }

    @RequestMapping(value = "/mealTypes/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_REMOVE_MEAL')")
    public MealCategory removeMealCategoryById(@PathVariable Long id) throws Exception {
        return mealCategoryService.remove(id);
    }

    @RequestMapping(value = "/mealTypes/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_EDIT_MEAL')")
    public MealCategory updateMealCategory(@PathVariable Long id, @RequestBody @Valid MealCategory meal) throws Exception {
        return mealCategoryService.update(id, meal);
    }
}
