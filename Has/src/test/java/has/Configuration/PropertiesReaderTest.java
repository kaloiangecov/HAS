package has.Configuration;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by Chokleet on 18.3.2017 г..
 */
public class PropertiesReaderTest {

    @Test
    public void readPropertiesTest() throws IOException, FileNotFoundException, URISyntaxException {
        PropertiesReader reader = new PropertiesReader();
        String result = reader.readProperty("overnightPrice");
        Assert.assertEquals("25", result);
    }

    @Test
    public void overwriteTest() throws IOException, URISyntaxException {
        PropertiesReader reader = new PropertiesReader();
        reader.editProperty("overnightPrice", "30");
        String result = reader.readProperty("overnightPrice");
        Assert.assertEquals("30", result);
    }

    @Test
    public void listTest() throws IOException, URISyntaxException {
        PropertiesReader reader = new PropertiesReader();
        reader.listProperties();
    }
}
