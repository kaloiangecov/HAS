package has.Request;

import has.ReservationGuest.ReservationGuest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

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
//    @NotNull
//    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "requests", cascade = CascadeType.ALL)
//    private List<Meal> meals;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "RESERVATION_GUEST_ID")
    private ReservationGuest reservationGuest;

    @NotNull
    @Min(0)
    @Max(2)
    private int status;

    private String timeFinished;

    @NotNull
    private String timePlaced;

    @NotNull
    @Min(0)
    @Max(2)
    private int type;

    public Request() {

    }
}
