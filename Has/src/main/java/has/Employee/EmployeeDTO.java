package has.Employee;

import has.Task.Task;
import has.WorkingSchedule.WorkingSchedule;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by kaloi on 3/18/2017.
 */
@Getter
@Setter
public class EmployeeDTO {

    private WorkingSchedule workingSchedule;

    private List<Task> tasks;

    private String sumDuration;

    public EmployeeDTO() {
        sumDuration = "00:00";
    }
}
