package has.Meal;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by kaloi on 12/20/2016.
 */
@Getter
@Setter
@Entity
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int code;
    private String date;
    private String description;
    private String name;
    private String price;
//    public Request m_Request;

    public Meal(){

    }
}
