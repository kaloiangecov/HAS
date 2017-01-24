package has.Employee;

import has.PersonalData.PersonalData;
import has.User.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @NotNull
    @OneToOne
    private User user;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PersonalData personalData;

    public Employee(String dateHired) {
        this.dateHired = dateHired;
    }

    public Employee(String dateHired, User user, PersonalData personalData) {
        this.dateHired = dateHired;
        this.user = user;
        this.personalData = personalData;
    }

    public Employee() {
    }
}
