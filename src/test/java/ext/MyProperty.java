package ext;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MyProperty {
    private static MyProperty instance;

    private Properties properties;

    private MyProperty() {}

    public static MyProperty getInstance() {
        if (instance == null) {
            instance = new MyProperty();
            instance.loadProperties();
        }

        return instance;
    }

    public Properties getProps() {
        return properties;
    }

    private void loadProperties() {
        properties = new Properties();
        try {
            properties.load(new FileReader("src/test/resources/my.properties"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
