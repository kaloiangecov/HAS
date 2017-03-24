package has.Task;

import has.Employee.Employee;
import has.Request.Request;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Chokleet on 25.2.2017 г..
 */
@Data
@Entity(name = "TASK")
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TASK_ID")
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    @Min(0)
    @Max(3)
    private Integer status;

    private String timePlaced;

    private String startTime;

    private String finishTime;

    private String targetTime;

    private String dueTime;

    @NotNull
    private Integer priority;

    @NotNull
    private String assigner;

    @NotNull
    private String duration;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee assignee;

    @OneToOne
    private Request request;

    public Task() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        timePlaced = sdf.format(new Date());
    }
}
