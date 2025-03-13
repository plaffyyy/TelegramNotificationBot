package backend.academy.bot.command_link_tests;

import static org.junit.jupiter.api.Assertions.*;

import backend.academy.bot.command_usage.Command;
import backend.academy.bot.command_usage.CommandHandler;
import backend.academy.bot.commands.TrackCommand;
import com.pengrad.telegrambot.TelegramBot;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public final class CommandHandleTests {
    @Mock
    private TelegramBot mockBot;

    @DisplayName("Проверка того, что программа правильно хендлит тип ссылок")
    @Test
    public void testCommandHandle() {

        CommandHandler commandHandler =
                new CommandHandler(mockBot, 111L, "/track https://github.com/plaffyyy/SpringMVCLearn");

        Command command = commandHandler.getCommandFromUpdate();

        assertInstanceOf(TrackCommand.class, command);
    }

    @DisplayName("Проверка того, что программа правильно парсит ссылки из сообщения")
    @ParameterizedTest
    @CsvSource({
        "/track https://github.com/plaffyyy/SpringMVCLearn, https://github.com/plaffyyy/SpringMVCLearn",
        "/untrack https://github.com/plaffyyy/SpringMVCLearn, https://github.com/plaffyyy/SpringMVCLearn"
    })
    public void testParseLink(String line, String url) {

        CommandHandler commandHandler = new CommandHandler(mockBot, 111L, line);

        Command command = commandHandler.getCommandFromUpdate();

        assertEquals(url, command.url());
    }

    @DisplayName("Проверка того, что программа неправильно парсит ссылки из сообщения")
    @ParameterizedTest
    @CsvSource({"/track", "/track https://github.com/plaffyyy/SpringMVCLearn dasdasdasdd", "/untrack"})
    public void testParseLinkIncorrect(String line) {

        CommandHandler commandHandler = new CommandHandler(mockBot, 111L, "/track");

        Command command = commandHandler.getCommandFromUpdate();

        assertNull(command);
    }
}
