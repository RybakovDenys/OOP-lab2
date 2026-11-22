package ua.lab2.greenhouse.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ua.lab2.greenhouse.model.Flower;

/** DOM-парсер. */
public class DomFlowerParser extends AbstractFlowerParser {

    @Override
    protected List<Flower> doParse(InputStream in)
            throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        // DefaultHandler кидає виняток при фатальній помилці й не друкує нічого в stderr
        documentBuilder.setErrorHandler(new DefaultHandler());
        Document document = documentBuilder.parse(in);

        NodeList nodes = document.getElementsByTagName(FlowerBuilder.FLOWER);
        List<Flower> flowers = new ArrayList<>(nodes.getLength());
        for (int i = 0; i < nodes.getLength(); i++) {
            flowers.add(buildFlower((Element) nodes.item(i)));
        }
        return flowers;
    }

    private static Flower buildFlower(Element element) {
        FlowerBuilder builder = new FlowerBuilder(element.getAttribute(FlowerBuilder.ID_ATTRIBUTE));
        for (String tag : FlowerBuilder.leafTags()) {
            NodeList children = element.getElementsByTagName(tag);
            if (children.getLength() > 0) {
                builder.set(tag, children.item(0).getTextContent().trim());
            }
        }
        return builder.build();
    }
}
