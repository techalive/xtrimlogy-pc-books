package us.awardspace.tekkno.xtrimlogy.order.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import us.awardspace.tekkno.xtrimlogy.order.application.port.QueryOrderUseCase;
import us.awardspace.tekkno.xtrimlogy.order.domain.Order;
import us.awardspace.tekkno.xtrimlogy.order.domain.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QueryOrderService implements QueryOrderUseCase {
    private final OrderRepository repository;

    @Override
    public List<Order> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void removeById(Long id) {
            repository.removeById(id);
    }
}
