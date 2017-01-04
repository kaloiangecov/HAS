package has.Reservation;

import has.Employee.Employee;
import has.User.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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
    private int discount;
    private String endDate;
    private boolean group;
//    private List<ReservationGuest> guests;
    @ManyToOne
    private User lastModifidBy;
    private String lastModifiedTime;
    private int numberAdults;
    private int numberChildren;
    private String price;

    @ManyToOne(fetch = FetchType.EAGER)
    private Employee receptionist;

    private String startDate;
    private int status;
}
