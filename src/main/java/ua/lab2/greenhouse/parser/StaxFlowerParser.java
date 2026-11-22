package ua.lab2.greenhouse.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import ua.lab2.greenhouse.model.Flower;

/** StAX-парсер (курсорний API). */
public class StaxFlowerParser extends AbstractFlowerParser {

    @Override
    protected List<Flower> doParse(InputStream in) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        XMLStreamReader reader = factory.createXMLStreamReader(in);
        List<Flower> flowers = new ArrayList<>();
        try {
            FlowerBuilder builder = null;
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    String name = reader.getLocalName();
                    if (FlowerBuilder.FLOWER.equals(name)) {
                        builder = new FlowerBuilder(reader.getAttributeValue(null, FlowerBuilder.ID_ATTRIBUTE));
                    } else if (builder != null && FlowerBuilder.isLeaf(name)) {
                        builder.set(name, reader.getElementText().trim());
                    }
                } else if (event == XMLStreamConstants.END_ELEMENT
                        && FlowerBuilder.FLOWER.equals(reader.getLocalName())) {
                    flowers.add(builder.build());
                    builder = null;
                }
            }
        } finally {
            reader.close();
        }
        return flowers;
    }
}
