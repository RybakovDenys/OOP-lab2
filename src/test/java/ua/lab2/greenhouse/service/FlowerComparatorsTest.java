package ua.lab2.greenhouse.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import ua.lab2.greenhouse.model.Flower;
import ua.lab2.greenhouse.parser.SaxFlowerParser;

public class FlowerComparatorsTest {

    private static List<Flower> load() throws Exception {
        return new ArrayList<>(new SaxFlowerParser().parse(Path.of("data", "flowers.xml")));
    }

    private static List<String> names(List<Flower> flowers) {
        return flowers.stream().map(Flower::getName).collect(Collectors.toList());
    }

    @Test
    public void sortsByNameIgnoringCase() throws Exception {
        List<Flower> flowers = load();
        flowers.sort(FlowerComparators.BY_NAME);
        assertEquals(List.of("Алое", "Бегонія", "Герань", "Кактус", "Орхідея", "Папороть", "Троянда", "Фіалка"),
                names(flowers));
    }

    @Test
    public void sortsByWatering() throws Exception {
        List<Flower> flowers = load();
        flowers.sort(FlowerComparators.BY_WATERING);
        assertEquals("Кактус", flowers.get(0).getName());
        assertEquals("Троянда", flowers.get(flowers.size() - 1).getName());
    }

    @Test
    public void sortsByAverageSize() throws Exception {
        List<Flower> flowers = load();
        flowers.sort(FlowerComparators.BY_AVERAGE_SIZE);
        assertEquals("Фіалка", flowers.get(0).getName());
        assertEquals("Троянда", flowers.get(flowers.size() - 1).getName());
    }

    @Test
    public void tiesAreBrokenByName() throws Exception {
        List<Flower> flowers = load();
        // при однаковому поливі порядок визначається назвою
        Flower a = flowers.get(0);
        Flower b = flowers.get(1);
        b.getTips().setWatering(a.getTips().getWatering());
        Comparator<Flower> comparator = FlowerComparators.BY_WATERING;
        int expected = Integer.signum(a.getName().compareToIgnoreCase(b.getName()));
        assertEquals(expected, Integer.signum(comparator.compare(a, b)));
    }
}
