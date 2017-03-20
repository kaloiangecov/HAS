package has.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * Created by Chokleet on 18.3.2017 г..
 */
public class PropertiesReader {

    private Properties properties;
    private InputStream iStream;
    private File file;

    public PropertiesReader() throws FileNotFoundException {
        properties = new Properties();
        file = new File("src/main/resources/config.txt");
        iStream = new FileInputStream(file);
    }

    public String readProperty(String key) throws IOException {
        properties.load(iStream);
        return properties.getProperty(key);
    }

    public Double readDouble(String key) throws IOException {
        properties.load(iStream);
        try{
            return Double.parseDouble(properties.getProperty(key));
        } catch(NumberFormatException e){
            return null;
        }
    }

    public void editProperty(String key, String value) throws IOException {
        properties.load(iStream);
        properties.setProperty(key,value);
        properties.store(new FileOutputStream(file),null);
    }

    public void listProperties() throws IOException {
        properties.load(iStream);
        System.out.println(properties.stringPropertyNames());
    }
}
