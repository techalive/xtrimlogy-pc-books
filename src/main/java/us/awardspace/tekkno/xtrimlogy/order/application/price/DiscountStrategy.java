package us.awardspace.tekkno.xtrimlogy.order.application.price;

import us.awardspace.tekkno.xtrimlogy.order.domain.Order;

import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal calculate(Order order);
}
