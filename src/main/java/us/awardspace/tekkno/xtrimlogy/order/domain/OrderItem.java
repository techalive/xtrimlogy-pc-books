package us.awardspace.tekkno.xtrimlogy.order.domain;

import lombok.Value;
import us.awardspace.tekkno.xtrimlogy.catalog.domain.Book;

@Value
public class OrderItem {
    Book book;
    int quantity;
}
