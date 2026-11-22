package ua.lab2.greenhouse.parser;

public class DomFlowerParserTest extends FlowerParserContractTest {

    @Override
    protected FlowerParser parser() {
        return new DomFlowerParser();
    }
}
