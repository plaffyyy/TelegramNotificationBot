package backend.academy.scrapper.database_config;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public abstract class DbConfigTest {

    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.17")
            .withDatabaseName("testDb")
            .withUsername("postgres")
            .withPassword("postgres");

    static {
        postgreSQLContainer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (postgreSQLContainer != null && postgreSQLContainer.isRunning()) {
                postgreSQLContainer.stop();
            }
        }));
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }
}
