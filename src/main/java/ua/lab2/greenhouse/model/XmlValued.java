package ua.lab2.greenhouse.model;

/** Перелік, кожна константа якого має текстове значення в XML. */
public interface XmlValued {

    String getXmlValue();

    /** Шукає константу переліку за значенням з XML. */
    static <E extends Enum<E> & XmlValued> E fromXml(Class<E> type, String value) {
        for (E constant : type.getEnumConstants()) {
            if (constant.getXmlValue().equals(value)) {
                return constant;
            }
        }
        throw new IllegalArgumentException("Unknown " + type.getSimpleName() + " value: " + value);
    }
}
