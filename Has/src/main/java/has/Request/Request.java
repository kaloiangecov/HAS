package has.Request;

import has.Meal.Meal;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "requests", cascade = CascadeType.ALL)
    private List<Meal> meals;
//    private Map<Meal,int> meals;
//    private ReservationGuest reservationGuest;
    private int status;
    private String timeFinished;
    private String timePlaced;
    private int type;

    public Request(){

    }
}
