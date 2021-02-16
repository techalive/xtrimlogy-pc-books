package us.awardspace.tekkno.xtrimlogy.order.application.port;

import us.awardspace.tekkno.xtrimlogy.order.domain.Order;

import java.util.List;
import java.util.Optional;

public interface QueryOrderUseCase {
    List<Order> findAll();

    Optional<Order> findById(Long id);

    void removeById(Long id);
}
