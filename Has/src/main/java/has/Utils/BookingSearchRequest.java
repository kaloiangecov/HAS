package has.Utils;

/**
 * Created by Stefan on 23.2.2017 г..
 */
public class BookingSearchRequest {
    private String startDate;
    private String endDate;
    private int numberAdults;
    private int numberChildren;
    private boolean pets;
    private boolean minibar;

    public BookingSearchRequest() {

    }

    public BookingSearchRequest(String startDate, String endDate, int numberAdults, int numberChildren, boolean pets, boolean minibar) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberAdults = numberAdults;
        this.numberChildren = numberChildren;
        this.pets = pets;
        this.minibar = minibar;
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

    public boolean isPets() {
        return pets;
    }

    public void setPets(boolean pets) {
        this.pets = pets;
    }

    public boolean isMinibar() {
        return minibar;
    }

    public void setMinibar(boolean minibar) {
        this.minibar = minibar;
    }
}
