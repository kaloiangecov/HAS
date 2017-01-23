package has.Meal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Chokleet on 20.1.2017 г..
 */
public class MealServiceTest {

    @InjectMocks
    MealService service;
    @Mock
    MealRepository repo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllEmployees() {
        List<Meal> meals = new LinkedList<>();
        Meal meal1 = new Meal();
        Meal meal2 = new Meal();
        Meal meal3 = new Meal();

        meals.add(meal1);
        meals.add(meal2);
        meals.add(meal3);
        Mockito.when(repo.findAll()).thenReturn(meals);
        assertEquals(service.getAllMeals(), meals);
        assertNotNull(service.getAllMeals());
    }

    @Test(expected = Exception.class)
    public void testFindByIdFail() throws Exception {
        service.findById(null);
    }

    @Test
    public void testFindById() throws Exception {
        Meal meal1 = new Meal();
        Mockito.when(repo.findOne(23456L)).thenReturn(meal1);
        assertNotNull(service.findById(23456L));
    }

    @Test(expected = Exception.class)
    public void testUpdateFail() throws Exception {
        service.findById(null);
    }

    @Test
    public void testUpdate() throws Exception {
        Meal meal1 = new Meal();
        Meal meal2 = new Meal();
        Mockito.when(repo.findOne(23456L)).thenReturn(meal1);
        Mockito.when(repo.save(meal2)).thenReturn(meal2);
        assertEquals(service.update(23456L, meal2), meal2);
    }
}
