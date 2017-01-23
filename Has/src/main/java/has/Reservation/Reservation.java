package has.Reservation;

import has.Employee.Employee;
import has.User.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by kaloi on 12/20/2016.
 */
@Getter
@Setter
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean allInclusive;
    private boolean breakfast;
    private boolean dinner;
    @NotNull
    @Pattern(regexp = "[0-9]")
    @Size(min = 0, max = 100)
    private int discount;
    @NotNull
    private String endDate;
    private boolean group;
    //    private List<ReservationGuest> guests;
    @ManyToOne
    private User lastModifidBy;
    private String lastModifiedTime;
    @NotNull
    @Size(min = 1)
    private int numberAdults;
    @NotNull
    @Size(min = 1)
    private int numberChildren;
    @NotNull
    private String price;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Employee receptionist;

    @NotNull
    private String startDate;
    @NotNull
    @Size(min = 0, max = 2)
    @Pattern(regexp = "[0-9]")
    private int status;
}
