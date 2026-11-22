package ua.lab2.greenhouse.model;

/** Спосіб розмноження. */
public enum Multiplying implements XmlValued {
    LEAVES("листям"),
    CUTTINGS("живцями"),
    SEEDS("насінням");

    private final String xmlValue;

    Multiplying(String xmlValue) {
        this.xmlValue = xmlValue;
    }

    @Override
    public String getXmlValue() {
        return xmlValue;
    }

    public static Multiplying fromXml(String value) {
        return XmlValued.fromXml(Multiplying.class, value);
    }
}
