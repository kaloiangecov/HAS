package has.Employee;

import has.PersonalData.PersonalData;
import has.User.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
    @Min(0)
    @Max(50)
    private int internship;

    @NotNull
    @OneToOne
    private User user;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PersonalData personalData;

    public Employee(String dateHired, int internship) {
        this.dateHired = dateHired;
        this.internship = internship;
    }

    public Employee(String dateHired, int internship, User user, PersonalData personalData) {
        this.dateHired = dateHired;
        this.internship = internship;
        this.user = user;
        this.personalData = personalData;
    }

    public Employee() {
    }
}
