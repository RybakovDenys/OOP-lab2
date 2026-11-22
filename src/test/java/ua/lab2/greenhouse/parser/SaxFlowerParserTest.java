package ua.lab2.greenhouse.parser;

public class SaxFlowerParserTest extends FlowerParserContractTest {

    @Override
    protected FlowerParser parser() {
        return new SaxFlowerParser();
    }
}
