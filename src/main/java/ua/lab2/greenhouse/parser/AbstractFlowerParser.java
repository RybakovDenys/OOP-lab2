package ua.lab2.greenhouse.parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
import ua.lab2.greenhouse.model.Flower;

/** Шаблонний метод: відкриття файлу, логування та обгортання помилок спільні для всіх парсерів. */
public abstract class AbstractFlowerParser implements FlowerParser {

    private static final Logger LOG = Logger.getLogger(AbstractFlowerParser.class.getName());

    @Override
    public final List<Flower> parse(Path xml) throws XmlProcessingException {
        String parserName = getClass().getSimpleName();
        LOG.log(Level.INFO, "{0}: parsing {1}", new Object[] {parserName, xml});
        try (InputStream in = Files.newInputStream(xml)) {
            List<Flower> flowers = doParse(in);
            LOG.log(Level.INFO, "{0}: parsed {1} flowers", new Object[] {parserName, flowers.size()});
            return flowers;
        } catch (IOException | SAXException | ParserConfigurationException
                 | XMLStreamException | IllegalArgumentException e) {
            LOG.log(Level.SEVERE, parserName + ": failed to parse " + xml, e);
            throw new XmlProcessingException("Cannot parse " + xml + ": " + e.getMessage(), e);
        }
    }

    protected abstract List<Flower> doParse(InputStream in)
            throws IOException, SAXException, ParserConfigurationException, XMLStreamException;
}
