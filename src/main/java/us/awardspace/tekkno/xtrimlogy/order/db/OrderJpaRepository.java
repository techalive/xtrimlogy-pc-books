package us.awardspace.tekkno.xtrimlogy.order.db;

import org.springframework.data.jpa.repository.JpaRepository;
import us.awardspace.tekkno.xtrimlogy.order.domain.Order;


public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
