package us.awardspace.tekkno.xtrimlogy.order.db;

import org.springframework.data.jpa.repository.JpaRepository;
import us.awardspace.tekkno.xtrimlogy.order.domain.Order;
import us.awardspace.tekkno.xtrimlogy.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;


public interface OrderJpaRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatusAndCreatedAtIsLessThanEqual(OrderStatus status, LocalDateTime timestamp);
}
