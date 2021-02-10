package us.awardspace.tekkno.xtrimlogy.order.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import us.awardspace.tekkno.xtrimlogy.order.application.port.QueryOrderUseCase;
import us.awardspace.tekkno.xtrimlogy.order.domain.Order;
import us.awardspace.tekkno.xtrimlogy.order.domain.OrderRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class QueryOrderService implements QueryOrderUseCase {
    private final OrderRepository repository;

    @Override
    public List<Order> findAll() {
        return repository.findAll();
    }
}
