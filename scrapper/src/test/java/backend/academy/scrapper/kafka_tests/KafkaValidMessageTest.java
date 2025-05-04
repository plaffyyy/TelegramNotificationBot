// package backend.academy.scrapper.kafka_tests;
//
// import static org.junit.jupiter.api.Assertions.*;
//
// import backend.academy.scrapper.ScrapperApplication;
// import backend.academy.scrapper.model.LinkUpdateRequest;
// import backend.academy.scrapper.repositories.LinkRepository;
// import backend.academy.scrapper.services.data.OrmLinkService;
// import java.util.concurrent.CountDownLatch;
// import java.util.function.Consumer;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.context.annotation.Import;
// import org.springframework.kafka.annotation.KafkaListener;
// import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.stereotype.Service;
// import org.springframework.test.annotation.DirtiesContext;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.testcontainers.junit.jupiter.Testcontainers;
// import org.testcontainers.utility.TestcontainersConfiguration;
//
// @SpringBootTest(
//        classes = {ScrapperApplication.class, KafkaValidMessageTest.TestKafkaListener.class},
//        properties = {
//            "spring.liquibase.enabled=false",
//
// "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration"
//        })
// @Import(TestcontainersConfiguration.class)
// @Testcontainers
// @DirtiesContext // для того чтобы контекст закрылся после теста
// public class KafkaValidMessageTest {
//
//    @MockitoBean
//    private LinkRepository linkRepository;
//
//    @MockitoBean
//    private OrmLinkService ormLinkService;
//
//    @Autowired
//    private KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;
//
//    @Value("${spring.kafka.topic.updates}")
//    private String updatesTopic;
//
//    @Autowired
//    private TestKafkaListener testListener;
//
//    /** Tests that a valid message is properly processed by the Kafka consumer */
//    @Test
//    void validMessageIsProcessed() throws InterruptedException {
//        // Set up a latch to wait for message processing
//        CountDownLatch latch = new CountDownLatch(1);
//        AtomicReference<LinkUpdateRequest> received = new AtomicReference<>();
//
//        // Configure the test listener to capture the message and signal completion
//        testListener.setLatch(latch);
//        testListener.setMessageConsumer(message -> {
//            received.set(message);
//            latch.countDown();
//        });
//
//        // Create a valid message
//        LinkUpdateRequest request =
//                new LinkUpdateRequest(123L, "https://example.com", "This is a test update", List.of(1L, 2L));
//
//        // Send the message
//        kafkaTemplate.send(updatesTopic, request.id().toString(), request);
//
//        // Wait for the consumer to process the message
//        boolean messageProcessed = latch.await(30, TimeUnit.SECONDS);
//
//        // Verify the message was processed
//        assertTrue(messageProcessed, "Message should be processed within the timeout");
//        assertNotNull(received.get(), "Message should be received by the consumer");
//        assertEquals(request.id(), received.get().id(), "Received message should have the same ID");
//        assertEquals(request.url(), received.get().url(), "Received message should have the same URL");
//        assertEquals(
//                request.description(),
//                received.get().description(),
//                "Received message should have the same description");
//        assertEquals(request.ids(), received.get().ids(), "Received message should have the same chat IDs");
//    }
//
//    /** Test helper service that listens for Kafka messages during tests */
//    @Service
//    public static class TestKafkaListener {
//        private CountDownLatch latch;
//        private java.util.function.Consumer<LinkUpdateRequest> messageConsumer;
//
//        public void setLatch(CountDownLatch latch) {
//            this.latch = latch;
//        }
//
//        public void setMessageConsumer(Consumer<LinkUpdateRequest> consumer) {
//            this.messageConsumer = consumer;
//        }
//
//        @KafkaListener(topics = "${spring.kafka.topic.updates}", groupId = "test-consumer-group")
//        public void listen(LinkUpdateRequest message) {
//            if (messageConsumer != null) {
//                messageConsumer.accept(message);
//            }
//        }
//    }
// }
