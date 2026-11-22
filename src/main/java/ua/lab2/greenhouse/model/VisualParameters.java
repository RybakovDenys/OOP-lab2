package ua.lab2.greenhouse.model;

import java.util.Objects;

/** Зовнішні параметри рослини. */
public class VisualParameters {

    private String stemColor;
    private String leafColor;
    /** Середній розмір, см. */
    private int averageSize;

    public String getStemColor() {
        return stemColor;
    }

    public void setStemColor(String stemColor) {
        this.stemColor = stemColor;
    }

    public String getLeafColor() {
        return leafColor;
    }

    public void setLeafColor(String leafColor) {
        this.leafColor = leafColor;
    }

    public int getAverageSize() {
        return averageSize;
    }

    public void setAverageSize(int averageSize) {
        this.averageSize = averageSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VisualParameters)) {
            return false;
        }
        VisualParameters that = (VisualParameters) o;
        return averageSize == that.averageSize
                && Objects.equals(stemColor, that.stemColor)
                && Objects.equals(leafColor, that.leafColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stemColor, leafColor, averageSize);
    }

    @Override
    public String toString() {
        return "VisualParameters{stemColor='" + stemColor + "', leafColor='" + leafColor
                + "', averageSize=" + averageSize + "}";
    }
}
