package has.Request;

import has.Meal.Meal;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by kaloi on 12/20/2016.
 */
@Getter
@Setter
@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "requests", cascade = CascadeType.ALL)
    private List<Meal> meals;
    //    private Map<Meal,int> meals;
//    private ReservationGuest reservationGuest;
    @NotNull
    @Size(min = 0, max = 2)
    private int status;
    private String timeFinished;
    @NotNull
    private String timePlaced;
    @NotNull
    @Size(min = 0, max = 2)
    private int type;

    public Request() {

    }
}
