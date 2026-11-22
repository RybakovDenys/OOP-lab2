package ua.lab2.greenhouse.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import ua.lab2.greenhouse.model.Flower;

public class FlowerParserFactoryTest {

    @Test
    public void createsParserOfRequestedType() {
        assertTrue(FlowerParserFactory.create(ParserType.SAX) instanceof SaxFlowerParser);
        assertTrue(FlowerParserFactory.create(ParserType.DOM) instanceof DomFlowerParser);
        assertTrue(FlowerParserFactory.create(ParserType.STAX) instanceof StaxFlowerParser);
    }

    @Test
    public void allParsersProduceTheSameCollection() throws Exception {
        Path xml = Path.of("data", "flowers.xml");
        List<Flower> sax = FlowerParserFactory.create(ParserType.SAX).parse(xml);
        List<Flower> dom = FlowerParserFactory.create(ParserType.DOM).parse(xml);
        List<Flower> stax = FlowerParserFactory.create(ParserType.STAX).parse(xml);
        assertEquals(sax, dom);
        assertEquals(sax, stax);
    }
}
