package ua.lab2.greenhouse.service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import ua.lab2.greenhouse.parser.XmlProcessingException;

/** Перетворення XML за допомогою XSL. */
public class XslTransformer {

    private static final Logger LOG = Logger.getLogger(XslTransformer.class.getName());

    public void transform(Path xml, Path xsl, Path output) throws XmlProcessingException {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer transformer = factory.newTransformer(new StreamSource(xsl.toFile()));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            Files.createDirectories(output.toAbsolutePath().getParent());
            try (OutputStream out = Files.newOutputStream(output)) {
                transformer.transform(new StreamSource(xml.toFile()), new StreamResult(out));
            }
            LOG.log(Level.INFO, "{0} transformed with {1} into {2}", new Object[] {xml, xsl, output});
        } catch (TransformerException | IOException e) {
            throw new XmlProcessingException("Cannot transform " + xml + ": " + e.getMessage(), e);
        }
    }
}
