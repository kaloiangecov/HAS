package has.Request;

import has.Employee.Employee;
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
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "RESERVATION_GUEST_ID")
    private ReservationGuest reservationGuest;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

    @NotNull
    @Min(0)
    @Max(2)
    private int status;

    private String timeFinished;

    @NotNull
    private String timePlaced;

    @NotNull
    @Min(0)
    @Max(10)
    @Column(name = "REQUEST_TYPE")
    private int type;

    //@OneToMany(mappedBy = "request", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //private List<RequestMeal> requestMeals;

    public Request() {

    }
}
