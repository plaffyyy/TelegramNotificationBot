package backend.academy.scrapper.exceptions;

public class ChatNotRegistered extends RuntimeException {
    public ChatNotRegistered(String message) {
        super(message);
    }
}
