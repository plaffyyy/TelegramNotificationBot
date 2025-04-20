package backend.academy.bot.controllers.kafka;

import backend.academy.bot.dto.LinkUpdateRequest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, LinkUpdateRequest> consumerFactory() {
        JsonDeserializer<LinkUpdateRequest> deserializer = new JsonDeserializer<>(LinkUpdateRequest.class);
        deserializer.setRemoveTypeHeaders(true);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(false);

        // в scrapper вся эта конфигурация написана в application.yml, здесь прописана вручную
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        // Start reading from latest messages for new consumer groups
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        // Enable auto-commit of offsets
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        // Set session timeout
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        // Set max poll interval
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "300000");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }
    //сюда инжектим consumerFactory, чтобы создавать Consumer по нашим настройкам
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest> kafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest>();
        factory.setConsumerFactory(consumerFactory());
        // можно добавить параллельность, если нужно(пока не нужно)
        factory.setConcurrency(1);
        // включаем пакетное чтение
        factory.setBatchListener(true);
        return factory;
    }
}
