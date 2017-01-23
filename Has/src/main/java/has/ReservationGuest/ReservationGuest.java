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
@Entity
@Getter
@Setter
public class ReservationGuest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEAL_ID")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Reservation reservation;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Guest guest;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Room room;

    private boolean isOwner;


}
