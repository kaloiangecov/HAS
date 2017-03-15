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

    public Double getReservationCost(int bedsSingle, int bedsDouble,
                                     boolean allInclusive, boolean dinner,
                                     boolean breakfast, int guestRang,
                                     int reservationDuration) {

        if (guestRang >= 10 && guestRang < 20) {
            guestDiscount = 0.05;
        } else if (guestRang >= 20 && guestRang < 30) {
            guestDiscount = 0.1;
        } else if (guestRang >= 30 && guestRang < 40) {
            guestDiscount = 0.15;
        } else if (guestRang >= 40) {
            guestDiscount = 0.2;
        }

        int allInclusiveValue = allInclusive ? 1 : 0;
        int dinnerValue = dinner ? 1 : 0;
        int breakfastValue = breakfast ? 1 : 0;

        return overnightPrice * (bedsSingle + bedsDouble * 1.5 +
                allInclusiveValue * allInclusivePrice + dinnerValue * dinnerPrice
                + breakfastValue * breakfastValue) * seasonalDiscount * guestDiscount * reservationDuration;
    }
}
