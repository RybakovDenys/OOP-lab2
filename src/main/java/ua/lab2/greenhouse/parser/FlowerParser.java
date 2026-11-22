package ua.lab2.greenhouse.parser;

import java.nio.file.Path;
import java.util.List;
import ua.lab2.greenhouse.model.Flower;

/** Читає XML-файл і створює колекцію об'єктів Flower. */
public interface FlowerParser {

    List<Flower> parse(Path xml) throws XmlProcessingException;
}
