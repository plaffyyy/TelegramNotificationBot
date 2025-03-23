package backend.academy.bot.exceptions;

public class IncorrectLinkForDelete extends RuntimeException {
    public IncorrectLinkForDelete(String message) {
        super(message);
    }
}
