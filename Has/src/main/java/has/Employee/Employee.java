package has.Employee;

import has.PersonalData.PersonalData;
import has.User.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by kaloi on 12/19/2016.
 */
@Getter
@Setter
@Entity
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String dateHired;
    private String post;

    @OneToOne
    private User user;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PersonalData personalData;
    //    public WorkingSchedule WorkingSchedule;
//    public Reservation reservation;

    public Employee(String dateHired, String post) {
        this.dateHired = dateHired;
        this.post = post;
    }

    public Employee() {
    }
}
