package ua.lab2.greenhouse.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class EnumsTest {

    @Test
    public void soilIsFoundByXmlValue() {
        for (Soil soil : Soil.values()) {
            assertEquals(soil, Soil.fromXml(soil.getXmlValue()));
        }
        assertEquals(Soil.SOD_PODZOLIC, Soil.fromXml("дерново-підзолистий"));
    }

    @Test
    public void multiplyingIsFoundByXmlValue() {
        for (Multiplying m : Multiplying.values()) {
            assertEquals(m, Multiplying.fromXml(m.getXmlValue()));
        }
        assertEquals(Multiplying.LEAVES, Multiplying.fromXml("листям"));
    }

    @Test
    public void unknownValueIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> Soil.fromXml("пісок"));
        assertThrows(IllegalArgumentException.class, () -> Multiplying.fromXml("діленням"));
        assertThrows(IllegalArgumentException.class, () -> Soil.fromXml(null));
    }
}
