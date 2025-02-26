package backend.academy.bot.command_link_tests;

import static org.junit.jupiter.api.Assertions.*;

import backend.academy.bot.command_usage.Command;
import backend.academy.bot.command_usage.CommandHandler;
import backend.academy.bot.commands.TrackCommand;
import com.pengrad.telegrambot.TelegramBot;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

public final class CommandHandleTests {

    @Test
    public void testCommandHandle() {

        TelegramBot mockBot = Mockito.mock(TelegramBot.class);
        CommandHandler commandHandler =
                new CommandHandler(mockBot, 111L, "/track https://github.com/plaffyyy/SpringMVCLearn");

        Command command = commandHandler.getCommandFromUpdate();

        assertInstanceOf(TrackCommand.class, command);
    }

    @ParameterizedTest
    @CsvSource({
        "/track https://github.com/plaffyyy/SpringMVCLearn, https://github.com/plaffyyy/SpringMVCLearn",
        "/untrack https://github.com/plaffyyy/SpringMVCLearn, https://github.com/plaffyyy/SpringMVCLearn"
    })
    public void testParseLink(String line, String url) {

        TelegramBot mockBot = Mockito.mock(TelegramBot.class);
        CommandHandler commandHandler = new CommandHandler(mockBot, 111L, line);

        Command command = commandHandler.getCommandFromUpdate();

        assertEquals(url, command.url());
    }

    @ParameterizedTest
    @CsvSource({"/track", "/track https://github.com/plaffyyy/SpringMVCLearn dasdasdasdd", "/untrack"})
    public void testParseLinkIncorrect(String line) {

        TelegramBot mockBot = Mockito.mock(TelegramBot.class);
        CommandHandler commandHandler = new CommandHandler(mockBot, 111L, "/track");

        Command command = commandHandler.getCommandFromUpdate();

        assertNull(command);
    }
}
