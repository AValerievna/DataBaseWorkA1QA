import java.io.IOException;
import java.util.Properties;

public class Configuration {
    private Properties prop;

    Configuration() throws IOException {
        prop = new Properties();
        prop.load(Configuration.class.getClassLoader().getResourceAsStream("configuration.properties"));
    }

    public String getProperty(String propName) {
        return prop.getProperty(propName);
    }

    public char getCharProperty(String propName) {
        return prop.getProperty(propName).charAt(0);
    }

    int getIntProperty(String propName) {
        return Integer.parseInt(prop.getProperty(propName));
    }
}