package fileLoaders;

import lombok.SneakyThrows;
import java.util.Properties;

public abstract class PropertiesReader {

    protected Properties properties;

    @SneakyThrows
    public PropertiesReader(String path) {
        properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream(path));
        loadData();
    }

    abstract void loadData();
}
