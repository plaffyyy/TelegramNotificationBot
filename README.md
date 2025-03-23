# Link Tracker

Проект сделан в рамках курса Академия Бэкенда.

Приложение для отслеживания обновлений контента по ссылкам.  
При появлении новых событий отправляется уведомление в Telegram.

Проект написан на Java 23 с использованием Spring Boot 3.

Проект состоит из 2-х приложений:
- Bot
- Scrapper

Для работы требуется БД PostgreSQL. Присутствует опциональная зависимость на Kafka.

## Использование

Для запуска приложения требуется установленный и запущенный Docker.  
Также в корне проекта нужно создать файл .env и прописать следующие ключи:
- TELEGRAM_TOKEN=<Ваш токен>
- GITHUB_TOKEN=<Ваш токен>

### Запуск

Для этого потребуется установленные Java 23, Maven, Docker.
- Запустить из модального окна IDEA [Run Anything](https://www.jetbrains.com/help/idea/running-anything.html) команду:

        mvn clean verify

- В консоли написать команду:

          docker-compose up --build

Для дополнительной справки: [HELP.md](./HELP.md)
