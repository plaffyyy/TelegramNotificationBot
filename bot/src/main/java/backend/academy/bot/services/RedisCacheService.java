package backend.academy.bot.services;

import backend.academy.bot.model.Link;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String LIST_CACHE_KEY_PREFIX = "bot:list:";
    @Value("${bot.cache.list-ttl}")
    private long cacheTtlSeconds; // Не static поле

    public Duration getCacheTtl() {
        return Duration.ofSeconds(cacheTtlSeconds);
    }

    public void cacheListResult(Long chatId, Set<Link> links) {
        String key = LIST_CACHE_KEY_PREFIX + chatId;
        try {
            redisTemplate.opsForValue().set(key, links, getCacheTtl());
            log.info("Cached list result for chatId: {}", chatId);
        } catch (Exception e) {
            log.error("Failed to cache list result for chatId: {}", chatId, e);
        }
    }

    @SuppressWarnings("unchecked")
    public Set<Link> getCachedList(Long chatId) {
        String key = LIST_CACHE_KEY_PREFIX + chatId;
        try {
            Set<Link> result = (Set<Link>) redisTemplate.opsForValue().get(key);
            if (result != null) {
                log.info("Retrieved cached list for chatId: {}", chatId);
            }
            return result;
        } catch (Exception e) {
            log.error("Failed to get cached list for chatId: {}", chatId, e);
            return null;
        }
    }

    public void changeListCache(Long chatId) {
        String key = LIST_CACHE_KEY_PREFIX + chatId;
        try {
            redisTemplate.delete(key);
            log.info("Invalidated list cache for chatId: {}", chatId);
        } catch (Exception e) {
            log.error("Failed to invalidate list cache for chatId: {}", chatId, e);
        }
    }
}
