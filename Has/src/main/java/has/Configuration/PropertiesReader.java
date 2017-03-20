package has.Configuration;

import java.io.*;
import java.util.Properties;

/**
 * Created by Chokleet on 18.3.2017 г..
 */
public class PropertiesReader {

    private Properties properties;
    private File file;

    public PropertiesReader() throws FileNotFoundException {
        properties = new Properties();
        file = new File("src/main/resources/config.txt");
    }

    public String readProperty(String key) throws IOException {
        properties.load(new FileInputStream(file));
        return properties.getProperty(key);
    }

    public Double readDouble(String key) throws IOException {
        properties.load(new FileInputStream(file));
        try {
            return Double.parseDouble(properties.getProperty(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void editProperty(String key, String value) throws IOException {
        properties.load(new FileInputStream(file));
        properties.setProperty(key, value);
        properties.store(new FileOutputStream(file), null);
    }

    public void listProperties() throws IOException {
        properties.load(new FileInputStream(file));
        System.out.println(properties.stringPropertyNames());
    }
}
