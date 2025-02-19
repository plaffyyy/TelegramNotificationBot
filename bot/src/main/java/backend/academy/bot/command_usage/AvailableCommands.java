package backend.academy.bot.command_usage;

import java.util.Map;

public final class AvailableCommands {
    public static final Map<String, String> commands = Map.of(
        "/start", "Регистрация пользователя.",
        "/help", "Вывод списка доступных команд.",
        "/track LINK", "Начать отслеживание ссылки.",
        "/untrack LINK", "Прекратить отслеживание ссылки.",
        "/list", "Показать список отслеживаемых ссылок (список ссылок, полученных при /track)."
    );
}
