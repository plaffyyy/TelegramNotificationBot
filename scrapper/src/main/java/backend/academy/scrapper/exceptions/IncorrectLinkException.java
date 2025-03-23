package backend.academy.scrapper.exceptions;

public class IncorrectLinkException extends RuntimeException {
    public IncorrectLinkException(String message) {
        super(message);
    }
}
