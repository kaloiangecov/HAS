package has.Guest;

import has.User.PersonalData;
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
public class Guest extends PersonalData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int numberReservations;
    private int status;

    @OneToOne
    private User user;
//    public ReservationGuest m_ReservationGuest;

    public Guest(){

    }
}
