package ua.lab2.greenhouse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;

public class LoggerConfigTest {

    @Test
    public void initConfiguresConsoleAndFileHandlers() {
        LoggerConfig.init();
        assertTrue(Files.isDirectory(Path.of("logs")));

        Logger root = Logger.getLogger("");
        assertEquals(Level.INFO, root.getLevel());
        boolean console = false;
        boolean file = false;
        for (Handler handler : root.getHandlers()) {
            console |= handler instanceof java.util.logging.ConsoleHandler;
            file |= handler instanceof java.util.logging.FileHandler;
        }
        assertTrue(console);
        assertTrue(file);
    }
}
