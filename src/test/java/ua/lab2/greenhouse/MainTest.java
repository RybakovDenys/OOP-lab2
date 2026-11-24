package ua.lab2.greenhouse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class MainTest {

    private static final String XML = Path.of("data", "flowers.xml").toString();
    private static final String XSD = Path.of("data", "flowers.xsd").toString();
    private static final String XSL = Path.of("data", "flowers.xsl").toString();

    private static Path tempDir() throws IOException {
        Path dir = Files.createTempDirectory("main-test");
        dir.toFile().deleteOnExit();
        return dir;
    }

    @Test
    public void runWithValidDataCreatesTransformedFile() throws Exception {
        Path out = tempDir();
        int code = Main.run(new String[] {XML, XSD, XSL, out.toString()});
        assertEquals(Main.EXIT_OK, code);
        assertTrue(Files.exists(out.resolve("flowers_by_soil.xml")));
    }

    @Test
    public void runWithInvalidXmlReturnsInvalidCode() throws Exception {
        Path bad = Files.createTempFile("bad", ".xml");
        bad.toFile().deleteOnExit();
        Files.writeString(bad, "<Flowers/>", StandardCharsets.UTF_8);
        int code = Main.run(new String[] {bad.toString(), XSD, XSL, tempDir().toString()});
        assertEquals(Main.EXIT_INVALID_XML, code);
    }

    @Test
    public void runWithMissingFileReturnsErrorCode() throws Exception {
        int code = Main.run(new String[] {"data/missing.xml", XSD, XSL, tempDir().toString()});
        assertEquals(Main.EXIT_ERROR, code);
    }

    @Test
    public void runWithDefaultArgumentsUsesProjectData() {
        assertEquals(Main.EXIT_OK, Main.run(new String[0]));
    }

    @Test
    public void mainDoesNotExitOnSuccess() {
        Main.main(new String[] {XML, XSD, XSL, "output"});
    }
}
