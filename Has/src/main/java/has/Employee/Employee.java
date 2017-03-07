package has.Employee;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import has.PersonalData.PersonalData;
import has.User.User;
import has.WorkingSchedule.WorkingSchedule;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created by kaloi on 12/19/2016.
 */
@Getter
@Setter
@Entity(name = "EMPLOYEE")
@Scope("session")
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EMPLOYEE_ID")
    private Long id;

    private String dateHired;

    @NotNull
    @OneToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "PERSONAL_DATA_ID")
    private PersonalData personalData;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<WorkingSchedule> workingSchedules;

    private boolean employed;

    public Employee(String dateHired) {
        this.dateHired = dateHired;
    }

    public Employee(String dateHired, User user, PersonalData personalData) {
        this.dateHired = dateHired;
        this.user = user;
        this.personalData = personalData;
    }

    public Employee() {
        employed = true;
    }

    public void setEmployed(boolean employed) {
        if (employed == false) {
            user.setIsEnabled(false);
        }
        this.employed = employed;
    }
}
