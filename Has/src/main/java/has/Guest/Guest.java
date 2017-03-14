package has.Guest;

import has.PersonalData.PersonalData;
import has.User.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by kaloi on 12/20/2016.
 */
@Getter
@Setter
@Entity(name = "GUEST")
@Scope("session")
public class Guest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "GUEST_ID")
    private Long id;

    @NotNull
    @Min(0)
    private int numberReservations;

    @NotNull
    @Min(0)
    @Max(2)
    private int status;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "PERSONAL_DATA_ID")
    private PersonalData personalData;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    private User user;

    public Guest(int status, User user, PersonalData personalData) {
        this.status = status;
        this.numberReservations = 1;
        this.personalData = personalData;
        this.user = user;
    }

    public Guest() {

    }
}
