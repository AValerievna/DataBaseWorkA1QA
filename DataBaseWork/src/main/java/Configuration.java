import java.io.IOException;
import java.util.Properties;

public class Configuration {
    private Properties prop;

    public Configuration() throws IOException {
        prop = new Properties();
        prop.load(Configuration.class.getClassLoader().getResourceAsStream("configuration.properties"));
    }

    public String getProperty(String propName) {
        return prop.getProperty(propName);
    }
}