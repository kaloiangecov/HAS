package has.RequestMeal;

import has.Meal.Meal;
import has.Request.Request;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by kaloi on 2/24/2017.
 */
@Getter
@Setter
@Entity(name = "REQUEST_MEAL")
@Scope("session")
public class RequestMeal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "REQUEST_MEAL_ID")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "MEAL_ID")
    private Meal meal;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "REQUEST_ID")
    private Request request;

    @NotNull
    private int quantity;
}