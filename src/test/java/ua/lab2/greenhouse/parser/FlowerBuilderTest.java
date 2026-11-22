package ua.lab2.greenhouse.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ua.lab2.greenhouse.model.Flower;
import ua.lab2.greenhouse.model.Multiplying;
import ua.lab2.greenhouse.model.Soil;

public class FlowerBuilderTest {

    @Test
    public void buildsFlowerFromTagValuePairs() {
        FlowerBuilder builder = new FlowerBuilder("flower-9");
        FlowerBuilder returned = builder
                .set(FlowerBuilder.NAME, "Алое")
                .set(FlowerBuilder.SOIL, "ґрунтовий")
                .set(FlowerBuilder.ORIGIN, "Африка")
                .set(FlowerBuilder.STEM_COLOR, "зелений")
                .set(FlowerBuilder.LEAF_COLOR, "сірий")
                .set(FlowerBuilder.AVERAGE_SIZE, "55")
                .set(FlowerBuilder.TEMPERATURE, "24")
                .set(FlowerBuilder.PHOTOPHILOUS, "true")
                .set(FlowerBuilder.WATERING, "200")
                .set(FlowerBuilder.MULTIPLYING, "живцями");
        assertSame(builder, returned);

        Flower f = builder.build();
        assertEquals("flower-9", f.getId());
        assertEquals("Алое", f.getName());
        assertEquals(Soil.GROUND, f.getSoil());
        assertEquals("Африка", f.getOrigin());
        assertEquals("зелений", f.getVisual().getStemColor());
        assertEquals("сірий", f.getVisual().getLeafColor());
        assertEquals(55, f.getVisual().getAverageSize());
        assertEquals(24, f.getTips().getTemperature());
        assertTrue(f.getTips().isPhotophilous());
        assertEquals(200, f.getTips().getWatering());
        assertEquals(Multiplying.CUTTINGS, f.getMultiplying());
    }

    @Test
    public void booleanAcceptsTrueAndOneAsTrue() {
        assertTrue(new FlowerBuilder("a").set(FlowerBuilder.PHOTOPHILOUS, "1").build().getTips().isPhotophilous());
        assertFalse(new FlowerBuilder("a").set(FlowerBuilder.PHOTOPHILOUS, "false").build().getTips().isPhotophilous());
        assertFalse(new FlowerBuilder("a").set(FlowerBuilder.PHOTOPHILOUS, "0").build().getTips().isPhotophilous());
    }

    @Test
    public void unknownTagAndBadValuesAreRejected() {
        FlowerBuilder builder = new FlowerBuilder("a");
        assertThrows(IllegalArgumentException.class, () -> builder.set("Unknown", "x"));
        assertThrows(IllegalArgumentException.class, () -> builder.set(FlowerBuilder.WATERING, "багато"));
        assertThrows(IllegalArgumentException.class, () -> builder.set(FlowerBuilder.SOIL, "пісок"));
    }

    @Test
    public void leafTagsAreRecognised() {
        assertTrue(FlowerBuilder.isLeaf(FlowerBuilder.NAME));
        assertTrue(FlowerBuilder.isLeaf(FlowerBuilder.MULTIPLYING));
        assertFalse(FlowerBuilder.isLeaf("VisualParameters"));
        assertFalse(FlowerBuilder.isLeaf(FlowerBuilder.FLOWER));
        assertEquals(10, FlowerBuilder.leafTags().size());
    }
}
