package ua.lab2.greenhouse.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import ua.lab2.greenhouse.model.Flower;
import ua.lab2.greenhouse.model.Multiplying;
import ua.lab2.greenhouse.model.Soil;

/** Однакові перевірки для всіх реалізацій FlowerParser (SAX, DOM, StAX). */
public abstract class FlowerParserContractTest {

    protected static final Path XML = Path.of("data", "flowers.xml");

    protected abstract FlowerParser parser();

    @Test
    public void parsesAllFlowersFromSampleFile() throws Exception {
        List<Flower> flowers = parser().parse(XML);
        assertEquals(8, flowers.size());

        Flower rose = flowers.get(0);
        assertEquals("flower-1", rose.getId());
        assertEquals("Троянда", rose.getName());
        assertEquals(Soil.PODZOLIC, rose.getSoil());
        assertEquals("Персія", rose.getOrigin());
        assertEquals("зелений", rose.getVisual().getStemColor());
        assertEquals("темно-зелений", rose.getVisual().getLeafColor());
        assertEquals(80, rose.getVisual().getAverageSize());
        assertEquals(18, rose.getTips().getTemperature());
        assertTrue(rose.getTips().isPhotophilous());
        assertEquals(1200, rose.getTips().getWatering());
        assertEquals(Multiplying.CUTTINGS, rose.getMultiplying());

        assertEquals(Soil.SOD_PODZOLIC, flowers.get(5).getSoil());
        assertEquals(Multiplying.LEAVES, flowers.get(3).getMultiplying());
    }

    @Test
    public void malformedXmlIsReportedAsProcessingException() throws IOException {
        Path file = temp("<Flowers><Flower id=\"flower-1\"></Flowers>");
        assertThrows(XmlProcessingException.class, () -> parser().parse(file));
    }

    @Test
    public void invalidValueIsReportedAsProcessingException() throws IOException {
        Path file = temp("<Flowers><Flower id=\"flower-1\"><Soil>пісок</Soil></Flower></Flowers>");
        assertThrows(XmlProcessingException.class, () -> parser().parse(file));
    }

    @Test
    public void missingFileIsReportedAsProcessingException() {
        assertThrows(XmlProcessingException.class, () -> parser().parse(Path.of("data", "missing.xml")));
    }

    @Test
    public void emptyListIsReturnedWhenThereAreNoFlowers() throws Exception {
        assertTrue(parser().parse(temp("<Flowers/>")).isEmpty());
    }

    private static Path temp(String content) throws IOException {
        Path file = Files.createTempFile("flowers-test", ".xml");
        file.toFile().deleteOnExit();
        Files.writeString(file, content, StandardCharsets.UTF_8);
        return file;
    }
}
