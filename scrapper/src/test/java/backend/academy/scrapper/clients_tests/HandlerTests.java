//package backend.academy.scrapper.clients_tests;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import backend.academy.scrapper.clients.Client;
//import backend.academy.scrapper.clients.ClientHandler;
//import backend.academy.scrapper.clients.GitHubClient;
//import backend.academy.scrapper.clients.StackOverflowClient;
//import backend.academy.scrapper.exceptions.UndefinedUrlException;
//import org.junit.Test;
//import org.junit.jupiter.api.DisplayName;
//
//public class HandlerTests {
//
//    @DisplayName("Проверка, что правильно распознает ссылку как Git")
//    @Test
//    public void clientHandleTestGit() {
//
//        String url = "https://github.com/plaffyyy/SpringMVCLearn";
//        ClientHandler clientHandler = new ClientHandler(null, null);
//
//        Client client = clientHandler.handleClients(url);
//
//        assertInstanceOf(GitHubClient.class, client);
//    }
//
//    @DisplayName("Проверка, что правильно распознает ссылку как StackOverflow")
//    @Test
//    public void clientHandleTestStack() {
//
//        String url = "https://stackoverflow.com/questions/79470759/react-and-typescript-state-is-of-type-unknown";
//        ClientHandler clientHandler = new ClientHandler(null, null);
//
//        Client client = clientHandler.handleClients(url);
//
//        assertInstanceOf(StackOverflowClient.class, client);
//    }
//
//    @DisplayName("Проверка, что при некорректной ссылке вылетает ошибка")
//    @Test
//    public void clientHandleTestUndefined() {
//
//        String url = "https://plaffyyy/SpringMVCLearn";
//        ClientHandler clientHandler = new ClientHandler(null, null);
//
//        boolean isUndefinedUrlException = false;
//        try {
//            Client client = clientHandler.handleClients(url);
//        } catch (UndefinedUrlException e) {
//            isUndefinedUrlException = true;
//        }
//
//        assertTrue(isUndefinedUrlException);
//    }
//}
