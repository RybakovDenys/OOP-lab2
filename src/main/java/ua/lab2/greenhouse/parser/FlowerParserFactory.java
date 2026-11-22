package ua.lab2.greenhouse.parser;

/** Фабрика парсерів. */
public final class FlowerParserFactory {

    private FlowerParserFactory() {
    }

    public static FlowerParser create(ParserType type) {
        return switch (type) {
            case SAX -> new SaxFlowerParser();
            case DOM -> new DomFlowerParser();
            case STAX -> new StaxFlowerParser();
        };
    }
}
