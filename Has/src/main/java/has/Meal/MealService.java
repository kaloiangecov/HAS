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
        return repo.findAll();
    }

    public Meal findById(Long id) throws Exception {
        Meal dbMeal = repo.findOne(id);
        if (dbMeal == null) {
            throw new Exception("There is no meal with such ID");
        }
        return dbMeal;
    }

    public Meal remove(Long id) throws Exception {
        Meal dbMeal = repo.findOne(id);
        if (dbMeal == null) {
            throw new Exception("There is no meal with such ID");
        }
        repo.delete(dbMeal);
        return dbMeal;
    }

    public Meal update(Long id, Meal meal) throws Exception {
        Meal dbMeal = repo.findOne(id);
        if (dbMeal == null) {
            throw new Exception("There is no meal with such ID");
        }
        dbMeal.setDate(meal.getDate());
        dbMeal.setDescription(meal.getDescription());
        dbMeal.setName(meal.getName());
        dbMeal.setPrice(meal.getPrice());

        return dbMeal;
    }

    public Page<Meal> search(int start, int length, String name) {
        PageRequest request = new PageRequest((start / length), length, Sort.Direction.ASC, "id");

        return repo.findByNameContaining(name, request);
    }
}
