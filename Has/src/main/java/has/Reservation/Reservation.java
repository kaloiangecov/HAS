package has.Reservation;

import has.Employee.Employee;
import has.User.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by kaloi on 12/20/2016.
 */
@Getter
@Setter
@Entity(name = "RESERVATION")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RESERVATION_ID")
    private Long id;

    private boolean allInclusive;
    private boolean breakfast;
    private boolean dinner;
    private boolean group;

    @NotNull
    //@Pattern(regexp = "[0-9]")
    @Min(0)
    @Max(100)
    private int discount;

    @NotNull
    private String startDate;

    @NotNull
    private String endDate;

    //    private List<ReservationGuest> guests;

    @ManyToOne
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "RESERVATION_EMPLOYEE", joinColumns = {
            @JoinColumn(name = "RESERVATION_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "EMPLOYEE_ID",
                    nullable = false, updatable = false)})
    private Employee receptionist;

    @NotNull
    @Min(0)
    @Max(2)
    //@Pattern(regexp = "[0-9]")
    private int status;
}
