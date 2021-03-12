package us.awardspace.tekkno.xtrimlogy.order.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.awardspace.tekkno.xtrimlogy.catalog.domain.Book;
import us.awardspace.tekkno.xtrimlogy.order.domain.OrderItem;
import us.awardspace.tekkno.xtrimlogy.order.domain.OrderStatus;
import us.awardspace.tekkno.xtrimlogy.order.domain.Recipient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RichOrderTest {

    @Test
    public void calculatesTotalPriceOfEmptyOrder() {
        RichOrder order = new RichOrder(
                1L,
                OrderStatus.NEW,
                Collections.emptySet(),
                Recipient.builder().build(),
                LocalDateTime.now()
        );
        BigDecimal price = order.totalPrice();

        assertEquals(BigDecimal.ZERO, price);
    }

    @Test
    public void calculatesTotalPrice() {
        //given
        Book book1 = new Book();
        book1.setPrice(new BigDecimal("12.50"));
        Book book2 = new Book();
        book2.setPrice((new BigDecimal("33.99")));
        Set<OrderItem> items = new HashSet<>(
                Arrays.asList(
                        new OrderItem(book1, 2),
                        new OrderItem(book2, 5)
                )
        );
        RichOrder order = new RichOrder(
                1L,
                OrderStatus.NEW,
                items,
                Recipient.builder().build(),
                LocalDateTime.now()
        );

        //when
        BigDecimal price = order.totalPrice();

        //then
        Assertions.assertEquals(new BigDecimal("194.95"), price);
    }

}