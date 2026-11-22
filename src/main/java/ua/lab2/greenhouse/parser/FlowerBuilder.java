package ua.lab2.greenhouse.parser;

import java.util.Set;
import ua.lab2.greenhouse.model.Flower;
import ua.lab2.greenhouse.model.GrowingTips;
import ua.lab2.greenhouse.model.Multiplying;
import ua.lab2.greenhouse.model.Soil;
import ua.lab2.greenhouse.model.VisualParameters;

/**
 * Збирає об'єкт Flower за парами "тег - текст". Спільний для SAX, DOM та StAX парсерів,
 * щоб не дублювати логіку перетворення тексту на значення полів.
 */
public final class FlowerBuilder {

    public static final String FLOWER = "Flower";
    public static final String ID_ATTRIBUTE = "id";

    public static final String NAME = "Name";
    public static final String SOIL = "Soil";
    public static final String ORIGIN = "Origin";
    public static final String STEM_COLOR = "StemColor";
    public static final String LEAF_COLOR = "LeafColor";
    public static final String AVERAGE_SIZE = "AverageSize";
    public static final String TEMPERATURE = "Temperature";
    public static final String PHOTOPHILOUS = "Photophilous";
    public static final String WATERING = "Watering";
    public static final String MULTIPLYING = "Multiplying";

    private static final Set<String> LEAF_TAGS = Set.of(
            NAME, SOIL, ORIGIN, STEM_COLOR, LEAF_COLOR, AVERAGE_SIZE,
            TEMPERATURE, PHOTOPHILOUS, WATERING, MULTIPLYING);

    private final Flower flower = new Flower();
    private final VisualParameters visual = new VisualParameters();
    private final GrowingTips tips = new GrowingTips();

    public FlowerBuilder(String id) {
        flower.setId(id);
        flower.setVisual(visual);
        flower.setTips(tips);
    }

    /** Чи є тег листовим елементом (містить текст, а не вкладені елементи). */
    public static boolean isLeaf(String tag) {
        return LEAF_TAGS.contains(tag);
    }

    public static Set<String> leafTags() {
        return LEAF_TAGS;
    }

    public FlowerBuilder set(String tag, String text) {
        switch (tag) {
            case NAME -> flower.setName(text);
            case SOIL -> flower.setSoil(Soil.fromXml(text));
            case ORIGIN -> flower.setOrigin(text);
            case STEM_COLOR -> visual.setStemColor(text);
            case LEAF_COLOR -> visual.setLeafColor(text);
            case AVERAGE_SIZE -> visual.setAverageSize(Integer.parseInt(text));
            case TEMPERATURE -> tips.setTemperature(Integer.parseInt(text));
            case PHOTOPHILOUS -> tips.setPhotophilous("true".equals(text) || "1".equals(text));
            case WATERING -> tips.setWatering(Integer.parseInt(text));
            case MULTIPLYING -> flower.setMultiplying(Multiplying.fromXml(text));
            default -> throw new IllegalArgumentException("Unsupported tag: " + tag);
        }
        return this;
    }

    public Flower build() {
        return flower;
    }
}
