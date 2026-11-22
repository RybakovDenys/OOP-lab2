package ua.lab2.greenhouse.model;

import java.util.Objects;

/** Умови вирощування. */
public class GrowingTips {

    /** Температура, °C. */
    private int temperature;
    /** true, якщо рослина світлолюбна. */
    private boolean photophilous;
    /** Полив, мл на тиждень. */
    private int watering;

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public boolean isPhotophilous() {
        return photophilous;
    }

    public void setPhotophilous(boolean photophilous) {
        this.photophilous = photophilous;
    }

    public int getWatering() {
        return watering;
    }

    public void setWatering(int watering) {
        this.watering = watering;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GrowingTips)) {
            return false;
        }
        GrowingTips that = (GrowingTips) o;
        return temperature == that.temperature
                && photophilous == that.photophilous
                && watering == that.watering;
    }

    @Override
    public int hashCode() {
        return Objects.hash(temperature, photophilous, watering);
    }

    @Override
    public String toString() {
        return "GrowingTips{temperature=" + temperature + ", photophilous=" + photophilous
                + ", watering=" + watering + "}";
    }
}
