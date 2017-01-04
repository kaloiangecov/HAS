package has.ReservationGuest;

import has.Guest.Guest;
import has.Reservation.Reservation;
import has.Room.Room;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by kaloi on 1/4/2017.
 */
@Entity
@Getter
@Setter
public class ReservationGuest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEAL_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.EAGER)
    private Guest guest;

    @ManyToOne(fetch = FetchType.EAGER)
    private Room room;

    private boolean isOwner;


}
