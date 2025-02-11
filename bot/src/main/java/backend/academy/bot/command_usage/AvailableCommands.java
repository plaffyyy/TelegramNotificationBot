package backend.academy.bot.command_usage;

import java.util.Map;

public final class AvailableCommands {
    public static final Map<String, String> commands = Map.of(
        "/start", "Регистрация пользователя.",
        "/help", "Вывод списка доступных команд.",
        "/track", "Начать отслеживание ссылки.",
        "/untrack", "Прекратить отслеживание ссылки.",
        "/list", "Показать список отслеживаемых ссылок (список ссылок, полученных при /track)."
    );
}
