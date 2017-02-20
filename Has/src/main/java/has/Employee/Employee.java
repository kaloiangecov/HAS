package has.Employee;

import has.PersonalData.PersonalData;
import has.User.User;
import has.WorkingSchedule.WorkingSchedule;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
    private List<WorkingSchedule> workingSchedules;

    @NotNull
    @Min(0)
    @Max(2)
    //0 = unemployed, 1 = employed, 2 = on leave
    private int status;

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

    public void setStatus(int status) {
        if (status == 0) {
            user.setIsEnabled(false);
            this.status = status;
        }
        this.status = status;
    }
}
