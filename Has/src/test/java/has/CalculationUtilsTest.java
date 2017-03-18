package has;

import has.Utils.CalculationUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Chokleet on 18.3.2017 г..
 */
public class CalculationUtilsTest {

    @Test
    public void calculationTest() throws IOException {
        Double result = CalculationUtils.getReservationCost(1, 0, true, false, false, 0, 2, 0);
        Assert.assertEquals(63, result, 0);
    }
}
