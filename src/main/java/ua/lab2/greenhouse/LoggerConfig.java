package ua.lab2.greenhouse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.LogManager;

/** Налаштування логування з файлу logging.properties (classpath). */
public final class LoggerConfig {

    private static final String CONFIG_RESOURCE = "/logging.properties";
    private static final Path LOG_DIRECTORY = Path.of("logs");

    private LoggerConfig() {
    }

    public static void init() {
        try (InputStream in = LoggerConfig.class.getResourceAsStream(CONFIG_RESOURCE)) {
            // FileHandler не створює каталог сам
            Files.createDirectories(LOG_DIRECTORY);
            LogManager.getLogManager().readConfiguration(in);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot configure logging", e);
        }
    }
}
