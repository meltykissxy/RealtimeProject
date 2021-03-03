import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

public class PropertiesUtilJava {
    public static Properties load(String propertieName) throws IOException {
        Properties prop = new Properties();
        prop.load(new InputStreamReader(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(propertieName)), StandardCharsets.UTF_8));
        return prop;
    }

    public static void main(String[] args) throws IOException {
        Properties properties = load("config.properties");
        System.out.println(properties.getProperty("elasticsearch.server"));
    }
}
