package pl.kurs.exception;

public class UploadInProgressException extends RuntimeException {

    public UploadInProgressException(String message) {
        super(message);
    }
}
