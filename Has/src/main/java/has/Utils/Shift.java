package has.Utils;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kaloi on 3/19/2017.
 */
@Getter
@Setter
public class Shift {
    private String startShift;
    private String endShift;

    public Shift(String startShift, String endShift) {
        this.startShift = startShift;
        this.endShift = endShift;
    }
}
