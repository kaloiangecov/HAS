package has.WorkingSchedule;

import has.Employee.Employee;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by Chokleet on 20.12.2016 г..
 */
@Getter
@Setter
@Entity(name = "WORKING_SCHEDULE")
public class WorkingSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String endDate;

    @NotNull
    @Min(0)
    @Max(3)
    private Integer shift;

    @NotNull
    private String startDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

}
