package us.awardspace.tekkno.xtrimlogy.order.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import us.awardspace.tekkno.xtrimlogy.catalog.db.BookJpaRepository;
import us.awardspace.tekkno.xtrimlogy.order.application.port.QueryOrderUseCase;
import us.awardspace.tekkno.xtrimlogy.order.db.OrderJpaRepository;
import us.awardspace.tekkno.xtrimlogy.order.domain.Order;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QueryOrderService implements QueryOrderUseCase {
    private final OrderJpaRepository repository;
    private final BookJpaRepository catalogRepository;

    @Override
    @Transactional
    public List<RichOrder> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toRichOrder)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RichOrder> findById(Long id) {
        return repository.findById(id).map(this::toRichOrder);
    }

    private RichOrder toRichOrder(Order order) {
        return new RichOrder(
                order.getId(),
                order.getStatus(),
                order.getItems(),
                order.getRecipient(),
                order.getCreatedAt()
        );
    }
}