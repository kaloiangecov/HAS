package has.Request;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
//    private Map<Meal,int> meals;
//    private ReservationGuest reservationGuest;
    private int status;
    private String timeFinished;
    private String timePlaced;
    private int type;

    public Request(){

    }
}
