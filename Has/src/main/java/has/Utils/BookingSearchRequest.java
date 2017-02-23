package has.Utils;

/**
 * Created by Stefan on 23.2.2017 г..
 */
public class BookingSearchRequest {
    private String startDate;
    private String endDate;
    private int numberAdults;
    private int numberChildren;

    public BookingSearchRequest() {

    }

    public BookingSearchRequest(String startDate, String endDate, int numberAdults, int numberChildren) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberAdults = numberAdults;
        this.numberChildren = numberChildren;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getNumberAdults() {
        return numberAdults;
    }

    public void setNumberAdults(int numberAdults) {
        this.numberAdults = numberAdults;
    }

    public int getNumberChildren() {
        return numberChildren;
    }

    public void setNumberChildren(int numberChildren) {
        this.numberChildren = numberChildren;
    }
}
