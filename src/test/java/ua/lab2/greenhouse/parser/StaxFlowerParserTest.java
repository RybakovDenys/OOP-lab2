package ua.lab2.greenhouse.parser;

public class StaxFlowerParserTest extends FlowerParserContractTest {

    @Override
    protected FlowerParser parser() {
        return new StaxFlowerParser();
    }
}
