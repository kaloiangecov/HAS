package has.ReservationGuest;

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

    private boolean isOwner;


}
