package ua.lab2.greenhouse.service;

import java.util.Comparator;
import ua.lab2.greenhouse.model.Flower;

/** Компаратори для сортування колекції Flower. */
public final class FlowerComparators {

    public static final Comparator<Flower> BY_NAME =
            Comparator.comparing(Flower::getName, String.CASE_INSENSITIVE_ORDER);

    public static final Comparator<Flower> BY_WATERING =
            Comparator.comparingInt((Flower f) -> f.getTips().getWatering()).thenComparing(BY_NAME);

    public static final Comparator<Flower> BY_AVERAGE_SIZE =
            Comparator.comparingInt((Flower f) -> f.getVisual().getAverageSize()).thenComparing(BY_NAME);

    private FlowerComparators() {
    }
}
