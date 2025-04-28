package backend.academy.scrapper;

import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import liquibase.integration.spring.SpringLiquibase;
import javax.sql.DataSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.test.context.SpringBootTest;
// isolated from the "bot" module's containers!
@TestConfiguration(proxyBeanMethods = false)
@SpringBootTest
@Testcontainers
@ComponentScan("backend.academy.scrapper.services.data")
class TestcontainersConfiguration {
    

    @Bean
    @RestartScope //one container per test class
    @ServiceConnection(name = "redis")
    GenericContainer<?> redisContainer() {
        return new GenericContainer<>(DockerImageName.parse("redis:7-alpine")).withExposedPorts(6379);
    }

    @Bean
    @RestartScope
    @ServiceConnection
    KafkaContainer kafkaContainer() {
        return new KafkaContainer("apache/kafka-native:3.8.1").withExposedPorts(9092);
    }

    // @Bean
    // public SpringLiquibase liquibase(DataSource dataSource) {
    //     SpringLiquibase liquibase = new SpringLiquibase();
    //     liquibase.setChangeLog("classpath:migrations/changelog/master.xml");
    //     liquibase.setDataSource(dataSource);
    //     liquibase.setDefaultSchema("public");
    //     return liquibase;
    // }
}
