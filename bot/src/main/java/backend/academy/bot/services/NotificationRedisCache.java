package backend.academy.bot.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationRedisCache {

    private final RedisTemplate<String, Object> redisTemplate;
    public static final String NOTIFICATION_CACHE_KEY_PREFIX = "bot:notifications:";

    public void addNotification(Long chatId, String message) {
        String key = NOTIFICATION_CACHE_KEY_PREFIX + chatId;
        try {
            redisTemplate.opsForValue().set(key, message);
            log.info("New notification add in cache for chatId: {}", chatId);
        } catch (Exception e ) {
            log.error("Failed to add Notification in cache for chatId: {}", chatId);
        }
    }

    public Set<String> getAllChatIdsFromCache() {
        try {
            return redisTemplate.keys(NOTIFICATION_CACHE_KEY_PREFIX + "*");
        } catch (Exception e) {
            log.error("Failed to get chatId from the cache");
            return Set.of();
        }
    }

    public List<String> getNotificationsByChatId(Long chatId) {
        String key = NOTIFICATION_CACHE_KEY_PREFIX + chatId;
        try {
            // в redis, чтобы взять все элементы конечная граница должна быть -1
            List<Object> notifications = redisTemplate.opsForList().range(key, 0, -1);
            if (notifications != null && !notifications.isEmpty()) {
                return notifications.stream()
                    .map(Object::toString)
                    .toList();
            }
            //не пришло ни одного уведомления
            return List.of();
        } catch (Exception e) {
            log.error("Failed to get notification from chat id: {}", chatId);
            return List.of();
        }
    }

    public void clearNotificationsByChatId(Long chatId) {
        String key = NOTIFICATION_CACHE_KEY_PREFIX + chatId;
        try {
            redisTemplate.delete(key);
            log.info("Removed notification for chat id: {}", chatId);
        } catch (Exception e) {
            log.error("Error in process of deleting notifications by chat id: {}", chatId);
        }
    }

}
