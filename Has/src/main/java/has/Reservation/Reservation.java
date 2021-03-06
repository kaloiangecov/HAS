package has.Reservation;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import has.Employee.Employee;
import has.ReservationGuest.ReservationGuest;
import has.Room.Room;
import has.User.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by kaloi on 12/20/2016.
 */
@Getter
@Setter
@Entity(name = "RESERVATION")
@Scope("session")
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RESERVATION_ID")
    private Long id;

    private boolean allInclusive;
    private boolean breakfast;
    private boolean dinner;

    private String reservationCode;

    private String groupId;

    @NotNull
    @Min(0)
    @Max(100)
    private int discount;

    @NotNull
    private String startDate;

    @NotNull
    private String endDate;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee receptionist;

    @NotNull
    @Min(0)
    @Max(2)
    private int status;

    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "ROOM_ID")
    private Room room;

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ReservationGuest> reservationGuests;

    public Reservation() {
        UUID code = UUID.randomUUID();
        reservationCode = String.valueOf(code);
    }
}
