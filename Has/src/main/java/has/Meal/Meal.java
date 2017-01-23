package has.Meal;

import has.Request.Request;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by kaloi on 12/20/2016.
 */
@Getter
@Setter
@Entity
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEAL_ID")
    private Long id;
    @NotNull
    private int code;
    @NotNull
    private String date;
    @NotNull
    @Size(min = 3, max = 250)
    private String description;
    @NotNull
    @Size(min = 3, max = 50)
    private String name;
    @NotNull
    @Size(min = 0)
    private Double price;

    @NotNull
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(joinColumns = {
            @JoinColumn(name = "PERMISSION_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "MEAL_ID",
                    nullable = false, updatable = false)})
    public List<Request> requests;

    public Meal() {

    }
}
