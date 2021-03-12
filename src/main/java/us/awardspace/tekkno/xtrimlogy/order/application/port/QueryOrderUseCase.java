package us.awardspace.tekkno.xtrimlogy.order.application.port;

import us.awardspace.tekkno.xtrimlogy.order.application.RichOrder;

import java.util.List;
import java.util.Optional;

public interface QueryOrderUseCase {
    List<RichOrder> findAll();

    Optional<RichOrder> findById(Long id);

}
