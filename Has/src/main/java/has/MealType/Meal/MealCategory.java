package has.MealType.Meal;

import has.Meal.Meal;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


/**
 * @author gundev
 */
@Getter
@Setter
@Entity(name = "MEAL_CATEGORY")
public class MealCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEAL_CATEGORY_ID")
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    private String title;

    @NotNull
    @Size(min = 3)
    private String img;

    @NotNull
    @Size(min = 3, max = 50)
    private String description;

    //@JsonManagedReference
    @OneToMany(mappedBy = "mealCategory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Meal> meals;

    public MealCategory() {

    }
}
