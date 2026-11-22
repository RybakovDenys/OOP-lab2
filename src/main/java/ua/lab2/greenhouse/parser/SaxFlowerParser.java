package ua.lab2.greenhouse.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ua.lab2.greenhouse.model.Flower;

/** SAX-парсер. */
public class SaxFlowerParser extends AbstractFlowerParser {

    @Override
    protected List<Flower> doParse(InputStream in)
            throws IOException, SAXException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        FlowerHandler handler = new FlowerHandler();
        factory.newSAXParser().parse(in, handler);
        return handler.flowers;
    }

    private static final class FlowerHandler extends DefaultHandler {

        private final List<Flower> flowers = new ArrayList<>();
        private final StringBuilder text = new StringBuilder();
        private FlowerBuilder builder;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            text.setLength(0);
            if (FlowerBuilder.FLOWER.equals(qName)) {
                builder = new FlowerBuilder(attributes.getValue(FlowerBuilder.ID_ATTRIBUTE));
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            text.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            if (builder == null) {
                return;
            }
            if (FlowerBuilder.FLOWER.equals(qName)) {
                flowers.add(builder.build());
                builder = null;
            } else if (FlowerBuilder.isLeaf(qName)) {
                builder.set(qName, text.toString().trim());
            }
        }
    }
}
