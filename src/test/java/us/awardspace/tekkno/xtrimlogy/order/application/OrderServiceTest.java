package us.awardspace.tekkno.xtrimlogy.order.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import us.awardspace.tekkno.xtrimlogy.catalog.application.port.CatalogUseCase;
import us.awardspace.tekkno.xtrimlogy.catalog.db.BookJpaRepository;
import us.awardspace.tekkno.xtrimlogy.catalog.domain.Book;
import us.awardspace.tekkno.xtrimlogy.order.application.port.ManipulateOrderUseCase;
import us.awardspace.tekkno.xtrimlogy.order.application.port.ManipulateOrderUseCase.OrderItemCommand;
import us.awardspace.tekkno.xtrimlogy.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;
import us.awardspace.tekkno.xtrimlogy.order.application.port.ManipulateOrderUseCase.PlaceOrderResponse;
import us.awardspace.tekkno.xtrimlogy.order.application.port.ManipulateOrderUseCase.UpdateStatusCommand;
import us.awardspace.tekkno.xtrimlogy.order.application.port.QueryOrderUseCase;
import us.awardspace.tekkno.xtrimlogy.order.domain.Delivery;
import us.awardspace.tekkno.xtrimlogy.order.domain.OrderStatus;
import us.awardspace.tekkno.xtrimlogy.order.domain.Recipient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class OrderServiceTest {

    @Autowired
    BookJpaRepository bookRepository;
    @Autowired
    ManipulateOrderService service;
    @Autowired
    CatalogUseCase catalogUseCase;
    @Autowired
    QueryOrderUseCase queryOrderService;

    @Test
    public void userCanPlaceOrder() {

        Book effectiveJava = givenEffectiveJava(50L);
        Book jcip = givenJavaConcurrency(50L);
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new OrderItemCommand(effectiveJava.getId(), 15))
                .item(new OrderItemCommand(jcip.getId(), 10))
                .build();

        PlaceOrderResponse response = service.placeOrder(command);

        assertTrue(response.isSuccess());
        assertEquals( 35L, availableCopiesOf(effectiveJava));
        assertEquals( 40L, availableCopiesOf(jcip));
    }

    @Test
    public void userCanRevokeOrder() {
        //given
        Book effectiveJava = givenEffectiveJava(50L);
        String recipient = "marek@example.org";
        Long orderId = placeOrder(effectiveJava.getId(), 15, recipient);
        assertEquals( 35L, availableCopiesOf(effectiveJava));


        //when
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.CANCELED, recipient);
        service.updateOrderStatus(command);

        //then
        assertEquals( 50L, availableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.CANCELED, queryOrderService.findById(orderId).get().getStatus());
    }

    @Test
    public void userCannotRevokeOtherUsersOrder() {
        //given
        Book effectiveJava = givenEffectiveJava(50L);
        String adam = "adam@example.org";
        Long orderId = placeOrder(effectiveJava.getId(), 15, adam);
        assertEquals( 35L, availableCopiesOf(effectiveJava));


        //when
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.CANCELED, "marek@example.org");
        service.updateOrderStatus(command);

        //then
        assertEquals( 35L, availableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.NEW, queryOrderService.findById(orderId).get().getStatus());

    }

    @Test
    public void adminCanRevokeOtherUsersOrder() {
        //given
        Book effectiveJava = givenEffectiveJava(50L);
        String marek = "marek@example.org";
        Long orderId = placeOrder(effectiveJava.getId(), 15, marek);
        assertEquals( 35L, availableCopiesOf(effectiveJava));


        //when
        String admin = "admin@example.org";
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.CANCELED, admin);
        service.updateOrderStatus(command);

        //then
        assertEquals( 50L, availableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.CANCELED, queryOrderService.findById(orderId).get().getStatus());

    }

    @Test
    public void adminCanMarkOrderAsPaid() {
        //given
        Book effectiveJava = givenEffectiveJava(50L);
        String recipient = "marek@example.org";
        Long orderId = placeOrder(effectiveJava.getId(), 15, recipient);
        assertEquals( 35L, availableCopiesOf(effectiveJava));


        //when
        String admin = "admin@example.org";
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.PAID, admin);
        service.updateOrderStatus(command);

        //then
        assertEquals( 35L, availableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.PAID, queryOrderService.findById(orderId).get().getStatus());
    }

    @Test
    public void userCantOrderMoreBooksThanAvailable() {

        Book effectiveJava = givenEffectiveJava(5L);
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new OrderItemCommand(effectiveJava.getId(), 10))
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.placeOrder(command));

        assertTrue(exception.getMessage().contains("Too many copies of book " + effectiveJava.getId() + " requested"));
    }

    @Test
    public void shippingCostsAreAddedToTotalOrderPrice() {
        Book book = givenBook(50L, "49.90");

        Long orderId = placeOrder(book.getId(), 1);

        assertEquals("59.80", orderOf(orderId).getFinalPrice().toPlainString());
    }

    @Test
    public void shippingCostsAreDiscountedOver100zlotys() {
        Book book = givenBook(50L, "49.90");

        Long orderId = placeOrder(book.getId(), 3);

        RichOrder order = orderOf(orderId);
        assertEquals("149.70", order.getFinalPrice().toPlainString());
        assertEquals("149.70", order.getOrderPrice().getItemsPrice().toPlainString());
    }

    @Test
    public void cheapestBookIsHalfPricedWhenTotalOver200zlotys() {
        //given
        Book book = givenBook(50L, "49.90");
        //when
        Long orderId = placeOrder(book.getId(), 5);
        //then
        RichOrder order = orderOf(orderId);
        assertEquals("224.55", order.getFinalPrice().toPlainString());
    }

    @Test
    public void cheapestBookIsFreeWhenTotalOver400zlotys() {
        Book book = givenBook(50L, "49.90");

        Long orderId = placeOrder(book.getId(), 10);

        assertEquals("449.10", orderOf(orderId).getFinalPrice().toPlainString());
    }

    private RichOrder orderOf(Long orderId) {
        return queryOrderService.findById(orderId).get();
    }

    private Book givenBook(long available, String price) {
        return bookRepository.save(new Book("Java concurrency in Practice", 2006, new BigDecimal(price), available));
    }

    private Long placeOrder(Long bookId, int copies, String recipient) {
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient(recipient))
                .item(new OrderItemCommand(bookId, copies))
                .delivery(Delivery.COURIER)
                .build();
        return service.placeOrder(command).getRight();
    }

    private Long placeOrder(Long bookId, int copies) {
        return placeOrder(bookId, copies, "john@example.org");
    }

    private Long availableCopiesOf(Book effectiveJava) {
        return catalogUseCase.findById(effectiveJava.getId()).get().getAvailable();
    }
    private Book givenJavaConcurrency(long available) {
        return bookRepository.save(new Book("Java Concurrency in Practice", 2006, new BigDecimal("99.90"), available));
    }
    private Book givenEffectiveJava(long available) {
        return bookRepository.save(new Book("Effective Java", 2005, new BigDecimal("199.90"), available));
    }

    private Recipient recipient() {
        return recipient("john@example.org");
    }

    private Recipient recipient(String email) {
        return Recipient.builder().email(email).build();
    }

}