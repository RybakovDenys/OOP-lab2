package ua.lab2.greenhouse.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import ua.lab2.greenhouse.parser.XmlProcessingException;

public class XmlValidatorTest {

    private static final Path XML = Path.of("data", "flowers.xml");
    private static final Path XSD = Path.of("data", "flowers.xsd");

    private final XmlValidator validator = new XmlValidator();

    /** Правильна квітка, в якій можна підмінити окремі фрагменти. */
    private static String flowerXml(String id, String soil, String watering, String size) {
        return "<Flowers><Flower id=\"" + id + "\"><Name>Роза</Name><Soil>" + soil + "</Soil>"
                + "<Origin>Іспанія</Origin><VisualParameters><StemColor>зелений</StemColor>"
                + "<LeafColor>зелений</LeafColor><AverageSize unit=\"cm\">" + size + "</AverageSize>"
                + "</VisualParameters><GrowingTips><Temperature unit=\"celsius\">20</Temperature>"
                + "<Photophilous>true</Photophilous><Watering unit=\"ml-per-week\">" + watering
                + "</Watering></GrowingTips><Multiplying>насінням</Multiplying></Flower></Flowers>";
    }

    private static Path temp(String content) throws IOException {
        Path file = Files.createTempFile("validator-test", ".xml");
        file.toFile().deleteOnExit();
        Files.writeString(file, content, StandardCharsets.UTF_8);
        return file;
    }

    @Test
    public void sampleDocumentIsValid() throws Exception {
        assertTrue(validator.isValid(XML, XSD));
    }

    @Test
    public void minimalCorrectDocumentIsValid() throws Exception {
        assertTrue(validator.isValid(temp(flowerXml("flower-1", "ґрунтовий", "1", "500")), XSD));
    }

    @Test
    public void unknownSoilIsInvalid() throws Exception {
        assertFalse(validator.isValid(temp(flowerXml("flower-1", "пісок", "100", "10")), XSD));
    }

    @Test
    public void valuesOutOfBoundsAreInvalid() throws Exception {
        assertFalse(validator.isValid(temp(flowerXml("flower-1", "ґрунтовий", "0", "10")), XSD));
        assertFalse(validator.isValid(temp(flowerXml("flower-1", "ґрунтовий", "5001", "10")), XSD));
        assertFalse(validator.isValid(temp(flowerXml("flower-1", "ґрунтовий", "100", "501")), XSD));
    }

    @Test
    public void badIdIsInvalid() throws Exception {
        assertFalse(validator.isValid(temp(flowerXml("rose", "ґрунтовий", "100", "10")), XSD));
    }

    @Test
    public void missingRequiredAttributeIsInvalid() throws Exception {
        String xml = flowerXml("flower-1", "ґрунтовий", "100", "10").replace(" unit=\"cm\"", "");
        assertFalse(validator.isValid(temp(xml), XSD));
    }

    @Test
    public void duplicateIdIsInvalid() throws Exception {
        String one = flowerXml("flower-1", "ґрунтовий", "100", "10");
        String inner = one.substring("<Flowers>".length(), one.length() - "</Flowers>".length());
        assertFalse(validator.isValid(temp("<Flowers>" + inner + inner + "</Flowers>"), XSD));
    }

    @Test
    public void malformedXmlIsInvalid() throws Exception {
        assertFalse(validator.isValid(temp("<Flowers><Flower>"), XSD));
    }

    @Test
    public void missingXmlFileCausesException() {
        assertThrows(XmlProcessingException.class,
                () -> validator.isValid(Path.of("data", "missing.xml"), XSD));
    }

    @Test
    public void missingOrBrokenSchemaCausesException() throws Exception {
        assertThrows(XmlProcessingException.class,
                () -> validator.isValid(XML, Path.of("data", "missing.xsd")));
        Path brokenSchema = temp("<not-a-schema");
        assertThrows(XmlProcessingException.class, () -> validator.isValid(XML, brokenSchema));
    }
}
