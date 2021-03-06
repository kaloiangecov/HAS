package has.WorkingSchedule;

import has.Employee.Employee;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Chokleet on 20.12.2016 г..
 */
@Getter
@Setter
@Entity(name = "WORKING_SCHEDULE")
public class WorkingSchedule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "SCHEDULE_DATE")
    private String date;

    @NotNull
    @Min(0)
    @Max(2)
    private Integer shift; //0 morning, 1 lunch, 2 night

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

}
