package ua.lab2.greenhouse;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ua.lab2.greenhouse.model.Flower;
import ua.lab2.greenhouse.parser.FlowerParserFactory;
import ua.lab2.greenhouse.parser.ParserType;
import ua.lab2.greenhouse.parser.XmlProcessingException;
import ua.lab2.greenhouse.service.FlowerComparators;
import ua.lab2.greenhouse.service.XmlValidator;
import ua.lab2.greenhouse.service.XslTransformer;

/**
 * Точка входу.
 * Аргументи (необов'язкові): [xml] [xsd] [xsl] [каталог результатів].
 */
public final class Main {

    static final int EXIT_OK = 0;
    static final int EXIT_INVALID_XML = 1;
    static final int EXIT_ERROR = 2;

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    private Main() {
    }

    public static void main(String[] args) {
        LoggerConfig.init();
        int code = run(args);
        if (code != EXIT_OK) {
            System.exit(code);
        }
    }

    static int run(String[] args) {
        Path xml = Path.of(argument(args, 0, "data/flowers.xml"));
        Path xsd = Path.of(argument(args, 1, "data/flowers.xsd"));
        Path xsl = Path.of(argument(args, 2, "data/flowers.xsl"));
        Path outputDir = Path.of(argument(args, 3, "output"));

        try {
            if (!new XmlValidator().isValid(xml, xsd)) {
                LOG.log(Level.SEVERE, "Validation failed, stopping");
                return EXIT_INVALID_XML;
            }

            List<List<Flower>> results = new ArrayList<>();
            for (ParserType type : ParserType.values()) {
                List<Flower> flowers = FlowerParserFactory.create(type).parse(xml);
                flowers.sort(FlowerComparators.BY_NAME);
                LOG.log(Level.INFO, "{0}: flowers sorted by name:", type);
                flowers.forEach(flower -> LOG.info(flower.toString()));
                results.add(flowers);
            }
            boolean same = results.stream().allMatch(results.get(0)::equals);
            LOG.log(same ? Level.INFO : Level.WARNING, "SAX, DOM and StAX results equal: {0}", same);

            Path output = outputDir.resolve("flowers_by_soil.xml");
            new XslTransformer().transform(xml, xsl, output);
            return EXIT_OK;
        } catch (XmlProcessingException e) {
            LOG.log(Level.SEVERE, "Processing failed", e);
            return EXIT_ERROR;
        }
    }

    private static String argument(String[] args, int index, String defaultValue) {
        return args.length > index ? args[index] : defaultValue;
    }
}
