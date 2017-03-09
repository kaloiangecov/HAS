package has.ReservationGuest;

import com.fasterxml.jackson.annotation.JsonBackReference;
import has.Guest.Guest;
import has.Reservation.Reservation;
import has.Room.Room;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by kaloi on 1/4/2017.
 */
@Entity(name = "RESERVATION_GUEST")
@Getter
@Setter
@Scope("session")
public class ReservationGuest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RESERVATION_GUEST_ID")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "RESERVATION_ID")
    @JsonBackReference
    private Reservation reservation;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "GUEST_ID")
    private Guest guest;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Room room;

    @NotNull
    private boolean owner;
}
