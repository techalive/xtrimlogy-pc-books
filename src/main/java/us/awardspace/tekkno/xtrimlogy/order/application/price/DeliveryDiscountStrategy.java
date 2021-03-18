package us.awardspace.tekkno.xtrimlogy.order.application.price;

import us.awardspace.tekkno.xtrimlogy.order.domain.Order;

import java.math.BigDecimal;

public class DeliveryDiscountStrategy implements DiscountStrategy {

    public static final BigDecimal THRESHOLD = BigDecimal.valueOf(100);

    @Override
    public BigDecimal calculate(Order order) {
        if(order.getItemsPrice().compareTo(THRESHOLD) >= 0) {
            return order.getDeliveryPrice();
        }
        return BigDecimal.ZERO;
    }
}
