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
@Entity(name = "EMPLOYEE")
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EMPLOYEE_ID")
    private Long id;

    private String dateHired;

    @NotNull
    @OneToOne
    @JoinTable(name = "USER_EMPLOYEE", joinColumns = {
            @JoinColumn(name = "EMPLOYEE_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "USER_ID",
                    nullable = false, updatable = false)})
    private User user;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "EMPLOYEE_PERSONAL_DATA", joinColumns = {
            @JoinColumn(name = "EMPLOYEE_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "PERSONAL_DATA_ID",
                    nullable = false, updatable = false)})
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
