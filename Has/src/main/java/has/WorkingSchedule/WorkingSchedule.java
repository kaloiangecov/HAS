package has.WorkingSchedule;

import has.Employee.Employee;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by Chokleet on 20.12.2016 г..
 */
@Getter
@Setter
@Entity
public class WorkingSchedule {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String endDate;
    @NotNull
    @Pattern(regexp = "[0-9]+")
    private String shift;
    private String startDate;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Employee employee;

    public WorkingSchedule(String endDate, String shift, String startDate) {
        this.endDate = endDate;
        this.shift = shift;
        this.startDate = startDate;
    }
}
