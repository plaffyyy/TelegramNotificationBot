package backend.academy.bot.command_usage;

public final class FileWithTextResponses {
    // for start command
    public static final String firstWords = "Привет! Это регистрация в бот\nТебе надо писать эту команду каждый раз, когда ты удаляешь все ссылки\n"
        + "Уведомления с несуществующих ссылок приходить не будут, но создать ты их можешь";
    public static final String startInformation = "Напиши /help чтобы увидеть возможные команды.";
    public static final String successfulStart = "Чат зарегистрирован";
    public static final String errorStart = "Некорректные параметры запроса";

    // for help command
    public static final String helpWords = "Ты можешь пользоваться следующими командами:";

    // for track command
    public static final String successfulTrack = "Ссылка успешно добавлена";
    public static final String errorTrack = "Некорректные параметры запроса";

    // for untrack command

    // for list command
    public static final String listWords = "Вот ссылки с которых ты получаешь уведомления: ";
    public static final String errorList = "Ошибка при получении ссылок";
}
