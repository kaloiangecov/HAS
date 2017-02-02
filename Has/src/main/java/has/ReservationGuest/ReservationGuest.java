package has.ReservationGuest;

import has.Guest.Guest;
import has.Reservation.Reservation;
import has.Room.Room;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by kaloi on 1/4/2017.
 */
@Entity(name = "RESERVATION_GUEST")
@Getter
@Setter
public class ReservationGuest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RESERVATION_GUEST_ID")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "RESERVATION_ID")
    private Reservation reservation;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "GUEST_ID")
    private Guest guest;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Room room;

    @NotNull
    private boolean isOwner;


}
