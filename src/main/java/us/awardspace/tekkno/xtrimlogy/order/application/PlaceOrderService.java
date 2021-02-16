package us.awardspace.tekkno.xtrimlogy.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.awardspace.tekkno.xtrimlogy.order.application.port.PlaceOrderUseCase;
import us.awardspace.tekkno.xtrimlogy.order.domain.Order;
import us.awardspace.tekkno.xtrimlogy.order.domain.OrderRepository;

import java.util.Collections;

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

    @Override
    public PlaceOrderResponse updateOrder(UpdateOrderCommand command) {
        return repository
                .findById(command.getOrderId())
                .map(order -> {
                    Order updatedOrder = command.updateFields(order);
                    Order save = repository.save(updatedOrder);
                    return PlaceOrderResponse.success(save.getId());
                })
                .orElseGet(() -> new PlaceOrderResponse(false, command.getOrderId(), Collections.singletonList("Order NOT found with ID: " + command.getOrderId())));
    }
}
