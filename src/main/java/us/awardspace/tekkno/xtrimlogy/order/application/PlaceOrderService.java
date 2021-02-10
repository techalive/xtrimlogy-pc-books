package us.awardspace.tekkno.xtrimlogy.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.awardspace.tekkno.xtrimlogy.order.application.port.PlaceOrderUseCase;
import us.awardspace.tekkno.xtrimlogy.order.domain.Order;
import us.awardspace.tekkno.xtrimlogy.order.domain.OrderRepository;

@Service
@RequiredArgsConstructor
public class PlaceOrderService implements PlaceOrderUseCase {
    private final OrderRepository repository;

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderCommand command) {
        Order order = Order
                .builder()
                .recipient(command.getRecipient())
                .items(command.getItems())
                .build();
        Order save = repository.save(order);
        return PlaceOrderResponse.success(save.getId());
    }
}
