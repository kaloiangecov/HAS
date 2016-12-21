package has.Employee;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String fullName;
    private String post;

    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private User user;
    //    public WorkingSchedule m_WorkingSchedule;
//    public Reservation reservation;

    public Employee(String dateHired, String fullName, Long id, String post) {
        this.dateHired = dateHired;
        this.fullName = fullName;
        this.id = id;
        this.post = post;
    }

    public Employee() {
    }
}
