package has.Meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kaloi on 12/20/2016.
 */
@Service
public class MealService {

    @Autowired
    private MealRepository repo;

    public Meal save(Meal meal) {
        return repo.save(meal);
    }

    public List<Meal> getAllMeals() {
        List<Meal> meals = repo.findAll();

        for (Meal meal : meals) {
            meal = removeRecursions(meal);
        }

        return meals;
    }

    public Meal findById(Long id) throws Exception {
        Meal meal = repo.findOne(id);
        validateIdNotNull(meal);

        return (removeRecursions(meal));
    }

    public Meal remove(Long id) throws Exception {
        Meal meal = repo.findOne(id);
        validateIdNotNull(meal);
        repo.delete(meal);
        return (removeRecursions(meal));
    }

    public Meal update(Long id, Meal meal) throws Exception {
        Meal dbMeal = repo.findOne(id);
        validateIdNotNull(dbMeal);

        dbMeal.setDate(meal.getDate());
        dbMeal.setDescription(meal.getDescription());
        dbMeal.setName(meal.getName());
        dbMeal.setPrice(meal.getPrice());
        dbMeal.setImg(meal.getImg());

        return removeRecursions(repo.save(dbMeal));
    }

    public Page<Meal> search(int start, int length, String sortColumn, String sortDirection, String name) {
        PageRequest request = new PageRequest((start / length), length, Sort.Direction.fromString(sortDirection), sortColumn);

        Page<Meal> page = repo.findByNameContaining(name, request);

        for (Meal meal : page) {
            meal = removeRecursions(meal);
        }

        return page;
    }

    private Meal removeRecursions(Meal meal) {
        meal.getMealCategory().setMeals(null);
        return meal;
    }

    ;

    private void validateIdNotNull(Meal meal) throws Exception {
        if (meal == null) {
            throw new Exception("There is no meal with such ID");
        }
    }
}
