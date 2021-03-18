package us.awardspace.tekkno.xtrimlogy.order.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import us.awardspace.tekkno.xtrimlogy.catalog.application.port.CatalogUseCase;
import us.awardspace.tekkno.xtrimlogy.catalog.db.BookJpaRepository;
import us.awardspace.tekkno.xtrimlogy.catalog.domain.Book;
import us.awardspace.tekkno.xtrimlogy.clock.Clock;
import us.awardspace.tekkno.xtrimlogy.order.application.port.ManipulateOrderUseCase;
import us.awardspace.tekkno.xtrimlogy.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;
import us.awardspace.tekkno.xtrimlogy.order.application.port.QueryOrderUseCase;
import us.awardspace.tekkno.xtrimlogy.order.domain.OrderStatus;
import us.awardspace.tekkno.xtrimlogy.order.domain.Recipient;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "app.orders.payment-period=1H")
@AutoConfigureTestDatabase
class AbandonedOrdersJobTest {
    @TestConfiguration
    static class TestConfig {
        @Bean
        public Clock.Fake clock() {
            return new Clock.Fake();
        }
    }

    @Autowired
    BookJpaRepository bookRepository;
    @Autowired
    AbandonedOrdersJob ordersJob;
    @Autowired
    ManipulateOrderService manipulateOrderService;
    @Autowired
    QueryOrderUseCase queryOrderService;
    @Autowired
    CatalogUseCase catalogUseCase;
    @Autowired
    Clock.Fake clock;

    @Test
    public void shouldMarkOrdersAsAbandoned() {
        // given - orders
        Book book = givenEffectiveJava(50L);
        Long orderId = placeOrder(book.getId(), 15);

        // when - run
        clock.tick(Duration.ofHours(2));
        ordersJob.run();

        // then - status changed

        assertEquals(OrderStatus.ABANDONED, queryOrderService.findById(orderId).get().getStatus());
        assertEquals( 50L, availableCopiesOf(book));


    }

    private Long placeOrder(Long bookId, int copies) {
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new ManipulateOrderUseCase.OrderItemCommand(bookId, copies))
                .build();
        return manipulateOrderService.placeOrder(command).getRight();
    }

    private Recipient recipient() {
        return Recipient.builder().email("marek@example.org").build();
    }

    private Book givenEffectiveJava(long available) {
        return bookRepository.save(new Book("Effective Java", 2005, new BigDecimal("199.90"), available));
    }

    private Long availableCopiesOf(Book effectiveJava) {
        return catalogUseCase.findById(effectiveJava.getId()).get().getAvailable();
    }

}