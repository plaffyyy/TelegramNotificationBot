package backend.academy.bot.services;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationRedisCache {

    private final RedisTemplate<String, String> redisStringTemplate;
    public static final String NOTIFICATION_CACHE_KEY_PREFIX = "bot:notifications:";

    public void addNotification(Long chatId, String message) {
        String key = NOTIFICATION_CACHE_KEY_PREFIX + chatId;
        try {
            redisStringTemplate.opsForList().rightPush(key, message);
            log.info("New notification add in cache for chatId: {}", chatId);
        } catch (Exception e) {
            log.error("Failed to add Notification in cache for chatId: {}", chatId);
        }
    }

    public Set<String> getAllChatIdsFromCache() {
        try {
            return redisStringTemplate.keys(NOTIFICATION_CACHE_KEY_PREFIX + "*");
        } catch (Exception e) {
            log.error("Failed to get chatId from the cache");
            return Set.of();
        }
    }

    public List<String> getNotificationsByChatId(Long chatId) {
        String key = NOTIFICATION_CACHE_KEY_PREFIX + chatId;
        try {
            // в redis, чтобы взять все элементы конечная граница должна быть -1
            return redisStringTemplate.opsForList().range(key, 0, -1);
        } catch (Exception e) {
            log.error("Failed to get notification from chat id: {}, with error: {}", chatId, e.getMessage());
            return List.of();
        }
    }

    public void clearNotificationsByChatId(Long chatId) {
        String key = NOTIFICATION_CACHE_KEY_PREFIX + chatId;
        try {
            redisStringTemplate.delete(key);
            log.info("Removed notification for chat id: {}", chatId);
        } catch (Exception e) {
            log.error("Error in process of deleting notifications by chat id: {}", chatId);
        }
    }
}
