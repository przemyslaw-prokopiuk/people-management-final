package pl.kurs.exception;

public class InvalidCsvFileException extends RuntimeException {

    public InvalidCsvFileException(String message) {
        super(message);
    }
}
