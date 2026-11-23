package ua.lab2.greenhouse.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ua.lab2.greenhouse.parser.XmlProcessingException;

public class XslTransformerTest {

    private static final Path XML = Path.of("data", "flowers.xml");
    private static final Path XSL = Path.of("data", "flowers.xsl");

    private final XslTransformer transformer = new XslTransformer();

    private static Path tempDir() throws IOException {
        Path dir = Files.createTempDirectory("xsl-test");
        dir.toFile().deleteOnExit();
        return dir;
    }

    private static List<Element> childElements(Element parent) {
        List<Element> result = new ArrayList<>();
        NodeList nodes = parent.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                result.add((Element) nodes.item(i));
            }
        }
        return result;
    }

    @Test
    public void groupsFlowersBySoil() throws Exception {
        Path out = tempDir().resolve("grouped.xml");
        transformer.transform(XML, XSL, out);

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(out.toFile());
        Element root = doc.getDocumentElement();
        assertEquals("Flowers", root.getTagName());

        List<Element> groups = childElements(root);
        assertEquals(3, groups.size());
        // порядок груп залежить від колації JVM, тому порівнюємо як множину
        Set<String> groupNames = new HashSet<>();
        for (Element group : groups) {
            groupNames.add(group.getTagName());
        }
        assertEquals(Set.of("ґрунтовий", "дерново-підзолистий", "підзолистий"), groupNames);

        int total = 0;
        for (Element group : groups) {
            List<Element> flowers = childElements(group);
            total += flowers.size();
            for (Element flower : flowers) {
                assertEquals("Flower", flower.getTagName());
                assertEquals(group.getTagName(), flower.getElementsByTagName("Soil").item(0).getTextContent());
            }
        }
        assertEquals(8, total);
        // всередині групи квітки відсортовані за назвою
        for (Element group : groups) {
            if (group.getTagName().equals("ґрунтовий")) {
                List<Element> flowers = childElements(group);
                assertEquals(3, flowers.size());
                assertEquals("Алое", flowers.get(0).getElementsByTagName("Name").item(0).getTextContent());
                assertEquals("Фіалка", flowers.get(2).getElementsByTagName("Name").item(0).getTextContent());
            }
        }
    }

    @Test
    public void createsMissingOutputDirectory() throws Exception {
        Path out = tempDir().resolve("nested").resolve("grouped.xml");
        transformer.transform(XML, XSL, out);
        assertTrue(Files.exists(out));
        assertTrue(Files.readString(out, StandardCharsets.UTF_8).contains("<Flower id=\"flower-1\">"));
    }

    @Test
    public void brokenStylesheetCausesException() throws Exception {
        Path badXsl = Files.createTempFile("bad", ".xsl");
        badXsl.toFile().deleteOnExit();
        Files.writeString(badXsl, "<xsl:stylesheet", StandardCharsets.UTF_8);
        Path out = tempDir().resolve("x.xml");
        assertThrows(XmlProcessingException.class, () -> transformer.transform(XML, badXsl, out));
    }

    @Test
    public void missingInputCausesException() throws Exception {
        Path out = tempDir().resolve("x.xml");
        assertThrows(XmlProcessingException.class,
                () -> transformer.transform(Path.of("data", "missing.xml"), XSL, out));
    }
}
