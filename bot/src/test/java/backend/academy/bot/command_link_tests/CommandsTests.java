package backend.academy.bot.command_link_tests;

import backend.academy.bot.commands.ListCommand;
import backend.academy.bot.dto.LinkResponse;
import backend.academy.bot.model.Link;
import org.junit.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;


public class CommandsTests {

    @Test
    public void formatingLinksTest() {

        ListCommand listCommand = new ListCommand(1L, null);
        LinkResponse linkResponse = new LinkResponse(
            Set.of(
                new Link("https://github.com/plaffyyy/SpringMVCLearn", null, null),
                new Link("https://github.com/plaffyyy/ZdecEmptyLink", null, null)
            ), 2
        );

        String expectedFormatingLinks = "Отслеживаемые ссылки:\nhttps://github.com/plaffyyy/SpringMVCLearn\nhttps://github.com/plaffyyy/ZdecEmptyLink\n";
        String formatingLinks = listCommand.formatingLinks(linkResponse);

        assertEquals(expectedFormatingLinks, formatingLinks);


    }

}
