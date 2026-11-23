package ua.lab2.greenhouse.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;
import ua.lab2.greenhouse.parser.XmlProcessingException;

/** Валідація XML-документа за XSD-схемою. */
public class XmlValidator {

    private static final Logger LOG = Logger.getLogger(XmlValidator.class.getName());

    /**
     * @return true, якщо документ відповідає схемі; false, якщо він невалідний або не є well-formed
     * @throws XmlProcessingException якщо не вдалося прочитати файли або схема некоректна
     */
    public boolean isValid(Path xml, Path xsd) throws XmlProcessingException {
        Schema schema = loadSchema(xsd);
        try (InputStream in = Files.newInputStream(xml)) {
            schema.newValidator().validate(new StreamSource(in));
            LOG.log(Level.INFO, "{0} is valid against {1}", new Object[] {xml, xsd});
            return true;
        } catch (SAXException e) {
            // SAXParseException (з номером рядка) входить у повідомлення e.getMessage()
            LOG.log(Level.WARNING, "{0} is NOT valid: {1}", new Object[] {xml, e});
            return false;
        } catch (IOException e) {
            throw new XmlProcessingException("Cannot read " + xml + ": " + e.getMessage(), e);
        }
    }

    private static Schema loadSchema(Path xsd) throws XmlProcessingException {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            return factory.newSchema(xsd.toFile());
        } catch (SAXException e) {
            throw new XmlProcessingException("Cannot load schema " + xsd + ": " + e.getMessage(), e);
        }
    }
}
