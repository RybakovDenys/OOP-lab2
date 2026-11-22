package ua.lab2.greenhouse.model;

/** Тип ґрунту для посадки. */
public enum Soil implements XmlValued {
    PODZOLIC("підзолистий"),
    GROUND("ґрунтовий"),
    SOD_PODZOLIC("дерново-підзолистий");

    private final String xmlValue;

    Soil(String xmlValue) {
        this.xmlValue = xmlValue;
    }

    @Override
    public String getXmlValue() {
        return xmlValue;
    }

    public static Soil fromXml(String value) {
        return XmlValued.fromXml(Soil.class, value);
    }
}
