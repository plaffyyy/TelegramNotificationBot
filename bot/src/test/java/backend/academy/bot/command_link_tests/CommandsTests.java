package backend.academy.bot.command_link_tests;

import static org.junit.jupiter.api.Assertions.*;

import backend.academy.bot.commands.ListCommand;
import backend.academy.bot.dto.LinkResponse;
import backend.academy.bot.model.Link;
import java.util.Set;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

public class CommandsTests {

    @DisplayName("Проверка на форматирование ссылок")
    @Test
    public void formatingLinksTest() {

        ListCommand listCommand = new ListCommand(1L, null, null);
        LinkResponse linkResponse = new LinkResponse(
                Set.of(
                        new Link("https://github.com/plaffyyy/SpringMVCLearn", null, null),
                        new Link("https://github.com/plaffyyy/ZdecEmptyLink", null, null)),
                2);

        String formatingLinks = listCommand.formatingLinks(linkResponse);

        assertTrue(formatingLinks.contains("https://github.com/plaffyyy/SpringMVCLearn"));
        assertTrue(formatingLinks.contains("https://github.com/plaffyyy/ZdecEmptyLink"));
        assertTrue(formatingLinks.startsWith("Отслеживаемые ссылки:\n"));
    }
}
