package has.Meal;

import has.Request.Request;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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
    private int code;
    private String date;
    private String description;
    private String name;
    private String price;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(joinColumns = {
            @JoinColumn(name = "PERMISSION_ID", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "MEAL_ID",
                    nullable = false, updatable = false) })
    public List<Request> requests;

    public Meal(){

    }
}
