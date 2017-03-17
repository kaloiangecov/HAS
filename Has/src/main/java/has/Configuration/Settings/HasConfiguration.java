package has.Configuration.Settings;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by kaloi on 3/15/2017.
 */
@Entity(name = "CONFIGURATION")
@Getter
@Setter
public class HasConfiguration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CONFIGURATION_ID")
    private Long id;

    private double overnightPrice;

    private double singleBedPrice;

    private double doubleBedPrice;

    private double allInclusivePrice;

    private double breakfastPrice;

    private double dinnerPrice;

    private String hotelName;

    private double seasonalDiscount;

    private double guestDiscount;

    private double lowClassPrice;

    private double mediumClassPrice;

    private double highClassPrice;

}
