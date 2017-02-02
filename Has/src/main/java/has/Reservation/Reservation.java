package has.Reservation;

import has.Employee.Employee;
import has.User.User;
import lombok.Getter;
import lombok.Setter;

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
@Entity(name = "RESERVATION")
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RESERVATION_ID")
    private Long id;

    private boolean allInclusive;
    private boolean breakfast;
    private boolean dinner;
    private boolean group;

    @NotNull
    @Min(0)
    @Max(100)
    private int discount;

    @NotNull
    private String startDate;

    @NotNull
    private String endDate;

    //    private List<ReservationGuest> guests;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User lastModifiedBy;

    private String lastModifiedTime;

    @NotNull
    @Min(1)
    private int numberAdults;

    @NotNull
    @Min(0)
    private int numberChildren;

    @NotNull
    private Double price;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee receptionist;

    @NotNull
    @Min(0)
    @Max(2)
    private int status;

    public Reservation() {
    }
}
