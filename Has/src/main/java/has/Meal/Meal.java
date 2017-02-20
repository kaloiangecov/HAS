package has.Meal;

import has.Request.Request;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Created by kaloi on 12/20/2016.
 */
@Getter
@Setter
@Entity(name = "MEAL")
@Scope("session")
public class Meal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEAL_ID")
    private Long id;

    @NotNull
    private String date;

    @NotNull
    @Size(min = 3, max = 100)
    private String description;

    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    @NotNull
    private Double price;

    @NotNull
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "MEAL_REQUEST", joinColumns = {
            @JoinColumn(name = "MEAL_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "REQUEST_ID",
                    nullable = false, updatable = false)})
    public List<Request> requests;

    public Meal() {

    }
}
