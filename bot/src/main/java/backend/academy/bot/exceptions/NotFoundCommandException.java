package backend.academy.bot.exceptions;

public class NotFoundCommandException extends RuntimeException {
    public NotFoundCommandException(String message) {
        super(message);
    }
}
