package backend.academy.scrapper.exceptions;

public class UndefinedUrlException extends RuntimeException {
    public UndefinedUrlException(String message) {
        super(message);
    }
}
