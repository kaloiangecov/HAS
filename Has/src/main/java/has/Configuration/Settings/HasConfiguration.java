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

//    public Double getReservationCost(int bedsSingle, int bedsDouble,
//                                     boolean allInclusive, boolean dinner,
//                                     boolean breakfast, int guestRang,
//                                     int reservationDuration, int roomClass) {
//
//        double totalPrice;
//        int lowClass = 0;
//        int mediumClass = 0;
//        int highClass = 0;
//
//        switch (roomClass) {
//            case 2:
//                highClass = 1;
//                mediumClass = 0;
//                lowClass = 0;
//                break;
//            case 1:
//                highClass = 0;
//                mediumClass = 1;
//                lowClass = 0;
//                break;
//            case 0:
//                highClass = 0;
//                mediumClass = 0;
//                lowClass = 1;
//                break;
//        }
//
//        if (guestRang >= 10 && guestRang < 20) {
//            guestDiscount = 0.05;
//        } else if (guestRang >= 20 && guestRang < 30) {
//            guestDiscount = 0.1;
//        } else if (guestRang >= 30 && guestRang < 40) {
//            guestDiscount = 0.15;
//        } else if (guestRang >= 40) {
//            guestDiscount = 0.2;
//        }
//        double totalDiscount = 0;
//        if (seasonalDiscount + guestDiscount <= 0.4) {
//            totalDiscount = seasonalDiscount + guestDiscount;
//        }
//        int allInclusiveValue = allInclusive ? 1 : 0;
//        int dinnerValue = dinner ? 1 : 0;
//        int breakfastValue = breakfast ? 1 : 0;
//
//        totalPrice = reservationDuration * (overnightPrice *
//                (bedsSingle * singleBedPrice + bedsDouble * doubleBedPrice) *
//                (highClass * highClassPrice
//                        + mediumClass * mediumClassPrice + lowClass * lowClassPrice) +
//                allInclusiveValue * allInclusivePrice + dinnerValue * dinnerPrice
//                + breakfastValue * breakfastPrice);
//
////        System.out.println(reservationDuration + " * ( " + overnightPrice + " * (" + bedsSingle
////                + " * " + singleBedPrice + " + " + bedsDouble + " * " + doubleBedPrice + " ) * ( "
////                + highClass + " * " + highClassPrice + " + " + mediumClass + " * " + mediumClassPrice
////                + " + " + lowClass + " * " + lowClassPrice + " ) + " + allInclusiveValue + " * "
////                + allInclusivePrice + " + " + dinnerValue + " * " + dinnerPrice + "+ " + breakfastValue
////                + " * " + breakfastPrice + " )");
//
//        return totalPrice - (totalPrice * totalDiscount);
//    }
}
