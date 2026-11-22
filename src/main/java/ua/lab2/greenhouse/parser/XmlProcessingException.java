package ua.lab2.greenhouse.parser;

/** Помилка читання, валідації або перетворення XML. */
public class XmlProcessingException extends Exception {

    private static final long serialVersionUID = 1L;

    public XmlProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
