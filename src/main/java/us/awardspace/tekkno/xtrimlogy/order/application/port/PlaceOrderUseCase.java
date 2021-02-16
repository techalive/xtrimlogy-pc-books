package us.awardspace.tekkno.xtrimlogy.order.application.port;

import lombok.*;
import us.awardspace.tekkno.xtrimlogy.order.domain.Order;
import us.awardspace.tekkno.xtrimlogy.order.domain.OrderItem;
import us.awardspace.tekkno.xtrimlogy.order.domain.Recipient;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;

public interface PlaceOrderUseCase {
    PlaceOrderResponse placeOrder(PlaceOrderCommand command);

    PlaceOrderResponse updateOrder(UpdateOrderCommand command);

    @AllArgsConstructor
    @Builder
    @Value
    class PlaceOrderCommand {
        @Singular
        List<OrderItem> items;
        Recipient recipient;

    }

    @Value
    class PlaceOrderResponse {
        boolean success;
        Long orderId;
        List<String> errors;

        public static PlaceOrderResponse success(Long orderId) {
            return new PlaceOrderResponse(true, orderId, emptyList());
        }

        public static PlaceOrderResponse failure(String... errors) {
            return new PlaceOrderResponse(false, null, Arrays.asList(errors));
        }
    }

    @Value
    @Builder
    class UpdateOrderCommand {
        Long orderId;
        List<OrderItem> items;
        Recipient recipient;


        public UpdateOrderCommand(Long orderId, List<OrderItem> items, Recipient recipient) {
            this.orderId = orderId;
            this.items = items;
            this.recipient = recipient;
        }

        public Order updateFields(Order order) {
            if (items != null) {
                order.setItems(items);
            }
            if (recipient != null) {
                order.setRecipient(recipient);
            }
            return order;
        }
    }
}
