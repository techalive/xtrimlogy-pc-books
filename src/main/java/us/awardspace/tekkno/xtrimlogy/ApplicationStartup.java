package us.awardspace.tekkno.xtrimlogy;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import us.awardspace.tekkno.xtrimlogy.catalog.application.port.CatalogUseCase;
import us.awardspace.tekkno.xtrimlogy.catalog.application.port.CatalogUseCase.CreateBookCommand;
import us.awardspace.tekkno.xtrimlogy.catalog.application.port.CatalogUseCase.UpdateBookCommand;
import us.awardspace.tekkno.xtrimlogy.catalog.application.port.CatalogUseCase.UpdateBookResponse;
import us.awardspace.tekkno.xtrimlogy.catalog.domain.Book;
import us.awardspace.tekkno.xtrimlogy.order.application.port.PlaceOrderUseCase;
import us.awardspace.tekkno.xtrimlogy.order.application.port.PlaceOrderUseCase.PlaceOrderCommand;
import us.awardspace.tekkno.xtrimlogy.order.application.port.PlaceOrderUseCase.PlaceOrderResponse;
import us.awardspace.tekkno.xtrimlogy.order.application.port.QueryOrderUseCase;
import us.awardspace.tekkno.xtrimlogy.order.domain.OrderItem;
import us.awardspace.tekkno.xtrimlogy.order.domain.Recipient;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogUseCase catalog;
    private final PlaceOrderUseCase placeOrder;
    private final QueryOrderUseCase queryOrder;
    private final String title;
    private final String author;
    private final Long limit;

    public ApplicationStartup(CatalogUseCase catalog, PlaceOrderUseCase placeOrder, QueryOrderUseCase queryOrder, @Value("${xtrimlogy.catalog.query.title}") String title, @Value("${xtrimlogy.catalog.query.author}") String author, @Value("${xtrimlogy.catalog.limit}") Long limit) {
        this.catalog = catalog;
        this.placeOrder = placeOrder;
        this.queryOrder = queryOrder;
        this.title = title;
        this.author = author;
        this.limit = limit;
    }

    @Override
    public void run(String... args) {
        initData();
        searchCatalog();
        placeOrder();
    }

    private void placeOrder() {
        Book efektywneProgramowanie = catalog.findOneByTitle("Efektywne programowanie")
                .orElseThrow(() -> new IllegalStateException("Cannot find a book :-("));
        Book czystyKod = catalog.findOneByTitle("Czysty kod")
                .orElseThrow(() -> new IllegalStateException("Cannot find a book :-("));

        Recipient recipient = Recipient
                .builder()
                .name("Jan Kowalski")
                .phone("888-111-777")
                .street("Robotnicza 33")
                .city("Wrocław")
                .zipCode("55-000")
                .email("jkowalski@example.org")
                .build();

        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .item(new OrderItem(efektywneProgramowanie, 12))
                .item(new OrderItem(czystyKod, 50))
                .build();

        PlaceOrderResponse response = placeOrder.placeOrder(command);
        System.out.println("Created order with ID: " + response.getOrderId());

        queryOrder.findAll()
                .forEach(order -> {
                    System.out.println("Got order with total price: " + order.totalPrice() + " details: " + order);
                });
    }

    private void searchCatalog() {
        findByTitle();
        findByAuthor();
        findAndUpdate();
        findByTitle();
        findByAuthor();
    }

    private void initData() {
        catalog.addBook(new CreateBookCommand("Java dla zupełnie początkujących", "Tonny Gaddis", 2019, new BigDecimal ("21.90")));
        catalog.addBook(new CreateBookCommand("Czysty kod", "C. Martin", 2018, new BigDecimal ("29.90")));
        catalog.addBook(new CreateBookCommand("Kompedium programisty, wyd. X", "Herbert Schildt", 2019, new BigDecimal ("49.90")));
        catalog.addBook(new CreateBookCommand("Kompedium javowca, wyd. X", "Herbert Schildt", 2019, new BigDecimal ("48.90")));
        catalog.addBook(new CreateBookCommand("Efektywne programowanie", "Joshua Bloch", 2019, new BigDecimal ("47.90")));
    }

    private void findByAuthor() {
        List<Book> booksByAuthor = catalog.findByAuthor(author);
        booksByAuthor.stream().limit(limit).forEach(System.out::println);
    }

    private void findByTitle() {
        //	CatalogService service = new CatalogService();
        List<Book> booksByTitle = catalog.findByTitle(title);
        booksByTitle.stream().limit(limit).forEach(System.out::println);
    }

    private void findAndUpdate() {
        System.out.println("Updating book...");
        catalog.findOneByTitleAndAuthor("Kompedium javowca, wyd. X", "Herbert Schildt")
        .ifPresent(book -> {
            UpdateBookCommand command = UpdateBookCommand
                    .builder()
                    .id(book.getId())
                    .title("Kompendium programisty Javy, wyd XI")
                    .build();
            UpdateBookResponse response = catalog.updateBook(command);
            System.out.println("Updating book result: " + response.isSuccess());
        });
    }

}
