package has.Employee;

import has.Task.Task;
import has.WorkingSchedule.WorkingSchedule;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalTime;

import java.util.ArrayList;
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

    private static final int TASK_STATUS_IN_PROGRESS = 1;

    public EmployeeDTO() {
        this.sumDuration = "00:00";
    }

    public int getNumberOfTasksWithPriority(int priority) {
        int counter = 0;
        for (Task task : tasks) {
            if (task.getPriority() == priority) {
                counter++;
            }
        }
        return counter;
    }

    public String getSumDuration() {
        this.sumDuration = "00:00";
        for (Task task : tasks) {
            this.sumDuration =
                    addTime(LocalTime.parse(sumDuration),
                            LocalTime.parse(task.getDuration())).toString();
        }
        return sumDuration;
    }

    public String getSumDurationForPriority(int priority) {
        this.sumDuration = "00:00";

        for (Task task : tasks) {
            if (task.getPriority().equals(priority)) {
                this.sumDuration =
                        addTime(LocalTime.parse(sumDuration),
                                LocalTime.parse(task.getDuration())).toString();
            }
        }
        return sumDuration;
    }

    private LocalTime addTime(LocalTime time1, LocalTime time2) {
        time1 = time1.plusMinutes(time2.getMinuteOfHour());
        time1 = time1.plusHours(time2.getHourOfDay());
        return time1;
    }

    public List<Task> getTargetTimeTasks() {
        List<Task> targetTimeTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTargetTime() != null) {
                targetTimeTasks.add(task);
            }
        }
        return targetTimeTasks;
    }

    public Task getCurrentTask() {
        for (Task task : tasks) {
            if (task.getStatus() == TASK_STATUS_IN_PROGRESS) {
                return task;
            }
        }
        return null;
    }

    public void updateTask(Task task) {
        boolean taskUpdated = false;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()) {
                tasks.set(i, task);
                taskUpdated = true;
            }
        }
        if (taskUpdated == false) {
            tasks.add(task);
        }
    }
}