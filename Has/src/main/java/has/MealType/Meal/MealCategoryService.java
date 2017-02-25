package has.MealType.Meal;

import has.Meal.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by gundev on 19/02/2017.
 */
@Service
public class MealCategoryService {

    @Autowired
    private MealCategoryRepository repo;

    public MealCategory save(MealCategory meal) {
        return repo.save(meal);
    }

    public List<MealCategory> getAllMealCategories() {
        return repo.findAll();
    }

    public MealCategory findById(Long id) throws Exception {
        MealCategory dbMeal = repo.findOne(id);
        if (dbMeal == null) {
            throw new Exception("There is no meal with such ID");
        }
        return dbMeal;
    }

    public List<Meal> findMealsByCategoryId(Long id) {
        return repo.findMealByCategory(id);
    }

    public MealCategory remove(Long id) throws Exception {
        MealCategory dbMeal = repo.findOne(id);
        if (dbMeal == null) {
            throw new Exception("There is no meal with such ID");
        }
        repo.delete(dbMeal);
        return dbMeal;
    }

    public MealCategory update(Long id, MealCategory meal) throws Exception {
        MealCategory dbMeal = repo.findOne(id);
        if (dbMeal == null) {
            throw new Exception("There is no meal with such ID");
        }
        dbMeal.setTitle(meal.getTitle());
        dbMeal.setImg(meal.getImg());

        return dbMeal;
    }
}
