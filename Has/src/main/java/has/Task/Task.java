package has.Task;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Chokleet on 25.2.2017 г..
 */
@Data
@Entity(name="TASK")
public class Task implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TASK_ID")
    private Long id;
    private String title;
    private String description;
    private Integer status;
    private String timePlaced;
    private String startTime;
    private String finishTime;
    private Integer priority;

}
