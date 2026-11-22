package ua.lab2.greenhouse.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class FlowerModelTest {

    private static Flower flower() {
        VisualParameters visual = new VisualParameters();
        visual.setStemColor("зелений");
        visual.setLeafColor("світло-зелений");
        visual.setAverageSize(40);
        GrowingTips tips = new GrowingTips();
        tips.setTemperature(20);
        tips.setPhotophilous(true);
        tips.setWatering(700);
        Flower f = new Flower();
        f.setId("flower-1");
        f.setName("Герань");
        f.setSoil(Soil.PODZOLIC);
        f.setOrigin("Південна Африка");
        f.setVisual(visual);
        f.setTips(tips);
        f.setMultiplying(Multiplying.CUTTINGS);
        return f;
    }

    @Test
    public void gettersReturnWhatSettersStored() {
        Flower f = flower();
        assertEquals("flower-1", f.getId());
        assertEquals("Герань", f.getName());
        assertEquals(Soil.PODZOLIC, f.getSoil());
        assertEquals("Південна Африка", f.getOrigin());
        assertEquals("зелений", f.getVisual().getStemColor());
        assertEquals("світло-зелений", f.getVisual().getLeafColor());
        assertEquals(40, f.getVisual().getAverageSize());
        assertEquals(20, f.getTips().getTemperature());
        assertTrue(f.getTips().isPhotophilous());
        assertEquals(700, f.getTips().getWatering());
        assertEquals(Multiplying.CUTTINGS, f.getMultiplying());
    }

    @Test
    public void equalFlowersHaveEqualHashCodes() {
        Flower a = flower();
        Flower b = flower();
        assertEquals(a, a);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(a.getVisual(), b.getVisual());
        assertEquals(a.getVisual().hashCode(), b.getVisual().hashCode());
        assertEquals(a.getTips(), b.getTips());
        assertEquals(a.getTips().hashCode(), b.getTips().hashCode());
    }

    @Test
    public void differentFlowersAreNotEqual() {
        Flower a = flower();
        assertNotEquals(a, null);
        assertNotEquals(a, "text");
        assertFalse(a.getVisual().equals("text"));
        assertFalse(a.getTips().equals("text"));

        Flower b = flower();
        b.setName("Троянда");
        assertNotEquals(a, b);

        b = flower();
        b.getVisual().setAverageSize(41);
        assertNotEquals(a, b);
        assertNotEquals(a.getVisual(), b.getVisual());

        b = flower();
        b.getVisual().setStemColor("червоний");
        assertNotEquals(a.getVisual(), b.getVisual());

        b = flower();
        b.getVisual().setLeafColor("червоний");
        assertNotEquals(a.getVisual(), b.getVisual());

        b = flower();
        b.getTips().setPhotophilous(false);
        assertNotEquals(a.getTips(), b.getTips());

        b = flower();
        b.getTips().setTemperature(5);
        assertNotEquals(a.getTips(), b.getTips());

        b = flower();
        b.getTips().setWatering(1);
        assertNotEquals(a.getTips(), b.getTips());

        b = flower();
        b.setMultiplying(Multiplying.SEEDS);
        assertNotEquals(a, b);
    }

    @Test
    public void toStringContainsMainFields() {
        String text = flower().toString();
        assertTrue(text.contains("Герань"));
        assertTrue(text.contains("PODZOLIC"));
        assertTrue(text.contains("averageSize=40"));
        assertTrue(text.contains("watering=700"));
        assertTrue(text.contains("CUTTINGS"));
    }
}
