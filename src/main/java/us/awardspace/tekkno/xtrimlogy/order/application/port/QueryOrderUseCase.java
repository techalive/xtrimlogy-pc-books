package us.awardspace.tekkno.xtrimlogy.order.application.port;

import us.awardspace.tekkno.xtrimlogy.order.domain.Order;

import java.util.List;

public interface QueryOrderUseCase {
    List<Order> findAll();
}
