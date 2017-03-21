package has.Request;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import has.RequestMeal.RequestMeal;
import has.ReservationGuest.ReservationGuest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created by kaloi on 12/20/2016.
 */
@Getter
@Setter
@Entity(name = "REQUEST")
@Scope("session")
public class Request implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "REQUEST_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "RESERVATION_GUEST_ID")
    private ReservationGuest reservationGuest;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL)
    private List<RequestMeal> mealRequests;

    @NotNull
    @Min(0)
    @Max(2)
    private int status;

    private String timeFinished;

    private String timePlaced;

    private String targetTime;

    @NotNull
    @Min(0)
    @Max(10)
    @Column(name = "REQUEST_TYPE")
    private int type;

    public Request() {

    }
}
