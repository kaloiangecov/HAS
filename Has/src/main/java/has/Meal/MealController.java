package has.Meal;

import has.Utils.DataTableResult;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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
    @PreAuthorize("hasAuthority('PERM_CREATE_MEAL')")
    public Meal save(@RequestBody @Valid Meal meal) {
        return mealService.save(meal);
    }

    @RequestMapping(value = "/meals", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_MEAL')")
    public List<Meal> getAllMeals() {
        return mealService.getAllMeals();
    }

    @RequestMapping(value = "/meal/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_MEAL')")
    public Meal findMealById(@PathVariable Long id) throws Exception {
        return mealService.findById(id);
    }

    @RequestMapping(value = "/meal/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_REMOVE_MEAL')")
    public Meal removeMealById(@PathVariable Long id) throws Exception {
        return mealService.remove(id);
    }

    @RequestMapping(value = "/meal/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_EDIT_MEAL')")
    public Meal updateMeal(@PathVariable Long id, @RequestBody @Valid Meal meal) throws Exception {
        return mealService.update(id, meal);
    }

    @RequestMapping(value = "/meal/search", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_SEARCH_MEAL')")
    public
    @ResponseBody
    DataTableResult findMealByName(HttpServletRequest request) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();

        Page<Meal> meals = mealService.search(
                Integer.parseInt(parameterMap.get("start")[0]),
                Integer.parseInt(parameterMap.get("length")[0]),
                parameterMap.get("name")[0]);


        return new DataTableResult(
                Integer.parseInt(parameterMap.get("draw")[0]),
                Integer.parseInt(parameterMap.get("start")[0]),
                Integer.parseInt(parameterMap.get("length")[0]),
                meals.getTotalElements(),
                meals.getTotalElements(),
                meals.getContent());
    }
}
