package has.Guest;

import has.PersonalData.PersonalData;
import has.User.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by kaloi on 12/20/2016.
 */
@Getter
@Setter
@Entity
public class Guest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private int numberReservations;
    @NotNull
    @Size(max = 2, min = 0)
    private int status;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @NotNull
    private PersonalData personalData;

    @OneToOne
    private User user;

    public Guest(int status, User user, PersonalData personalData) {
        this.status = status;
        this.numberReservations = 1;
        this.personalData = personalData;
        this.user = user;
    }

    public Guest(){

    }
}
