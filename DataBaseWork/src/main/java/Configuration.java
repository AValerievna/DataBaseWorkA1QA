import java.io.IOException;
import java.util.Properties;

class Configuration {
    private Properties prop;

    Configuration() throws IOException {
        prop = new Properties();
        prop.load(Configuration.class.getClassLoader().getResourceAsStream("configuration.properties"));
    }

    String getProperty(String propName) {
        return prop.getProperty(propName);
    }
}