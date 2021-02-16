package us.awardspace.tekkno.xtrimlogy.order.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import us.awardspace.tekkno.xtrimlogy.order.application.port.PlaceOrderUseCase;
import us.awardspace.tekkno.xtrimlogy.order.application.port.PlaceOrderUseCase.PlaceOrderCommand;
import us.awardspace.tekkno.xtrimlogy.order.application.port.PlaceOrderUseCase.UpdateOrderCommand;
import us.awardspace.tekkno.xtrimlogy.order.application.port.QueryOrderUseCase;
import us.awardspace.tekkno.xtrimlogy.order.domain.Order;
import us.awardspace.tekkno.xtrimlogy.order.domain.OrderItem;
import us.awardspace.tekkno.xtrimlogy.order.domain.OrderStatus;
import us.awardspace.tekkno.xtrimlogy.order.domain.Recipient;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/orders")
@RestController
@AllArgsConstructor
public class OrderController {
    private final QueryOrderUseCase order;
    private final PlaceOrderUseCase place;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getAll() {
        return order.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable Long id) {
        return order
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addOrder(@Valid @RequestBody RestOrderCommand command) {
        PlaceOrderUseCase.PlaceOrderResponse order = place.placeOrder(command.toCreateCommand());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/" + order.getOrderId().toString()).build().toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateOrder(@PathVariable Long id, @RequestBody RestOrderCommand command) {
        PlaceOrderUseCase.PlaceOrderResponse response = place.updateOrder(command.toUpdateCommand(id));
        if(!response.isSuccess()) {
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        order.removeById(id);
    }

    @Data
    private static class RestOrderCommand {
        @NotNull(message = "Status is EMPTY! Please change it.")
        private OrderStatus status = OrderStatus.NEW;
        @NotNull(message = "Please provide item collect")
        private List<OrderItem> items;
        @NotNull(message = "Provide fully recipient")
        private Recipient recipient;
        @NotNull(message = "Must be not negative, but greater than 0")
        private LocalDateTime createdAt;

        PlaceOrderCommand toCreateCommand() {
             return new PlaceOrderCommand(items, recipient);
        }

        UpdateOrderCommand toUpdateCommand(Long id) {
             return new UpdateOrderCommand(id, items, recipient);
        }
    }
}
