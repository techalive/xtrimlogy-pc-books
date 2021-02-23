package us.awardspace.tekkno.xtrimlogy;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import us.awardspace.tekkno.xtrimlogy.catalog.application.port.CatalogUseCase;
import us.awardspace.tekkno.xtrimlogy.catalog.application.port.CatalogUseCase.CreateBookCommand;
import us.awardspace.tekkno.xtrimlogy.catalog.application.port.CatalogUseCase.UpdateBookCommand;
import us.awardspace.tekkno.xtrimlogy.catalog.application.port.CatalogUseCase.UpdateBookResponse;
import us.awardspace.tekkno.xtrimlogy.catalog.domain.Book;
import us.awardspace.tekkno.xtrimlogy.order.application.port.ManipulateOrderUseCase;
import us.awardspace.tekkno.xtrimlogy.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;
import us.awardspace.tekkno.xtrimlogy.order.application.port.QueryOrderUseCase;
import us.awardspace.tekkno.xtrimlogy.order.domain.OrderItem;
import us.awardspace.tekkno.xtrimlogy.order.domain.Recipient;

import java.math.BigDecimal;
import java.util.List;

import static us.awardspace.tekkno.xtrimlogy.order.application.port.ManipulateOrderUseCase.*;

@Component
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogUseCase catalog;
    private final ManipulateOrderUseCase placeOrder;
    private final QueryOrderUseCase queryOrder;
    private final String title;
    private final String author;
    private final Long limit;

    public ApplicationStartup(CatalogUseCase catalog, ManipulateOrderUseCase placeOrder, QueryOrderUseCase queryOrder, @Value("${xtrimlogy.catalog.query.title}") String title, @Value("${xtrimlogy.catalog.query.author}") String author, @Value("${xtrimlogy.catalog.limit}") Long limit) {
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
        firstOrder();
        secondOrder();
    }

    private void firstOrder() {
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
                .item(new OrderItem(efektywneProgramowanie.getId(), 12))
                .item(new OrderItem(czystyKod.getId(), 50))
                .build();

        orderSummary(command);
    }

    private void secondOrder() {
        Book javaKompendium = catalog.findOneByTitle("Kompedium programisty, wyd. X")
                .orElseThrow(() -> new IllegalStateException("Cannot find a book :-("));
        Book javowcaKompendium = catalog.findOneByTitle("Kompendium programisty Javy, wyd XI")
                .orElseThrow(() -> new IllegalStateException("Cannot find a book :-("));
        Recipient recipient = Recipient
                .builder()
                .name("Ada Nowa")
                .phone("777-700-303")
                .street("Lwowska 1a")
                .city("Kraków")
                .zipCode("25-100")
                .email("adanowa@example.org")
                .build();

        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .item(new OrderItem(javaKompendium.getId(), 9))
                .item(new OrderItem(javowcaKompendium.getId(), 16))
                .build();

        orderSummary(command);
    }

    private void orderSummary(PlaceOrderCommand command) {
        PlaceOrderResponse response = placeOrder.placeOrder(command);

        String result = response.handle(
                orderId -> "Created ORDER with id: " + orderId,
                error -> "Failed to created order: " + error
        );
        System.out.println(result);

        queryOrder.findAll()
                .forEach(order -> System.out.println("GOT ORDER WITH TOTAL PRICE: " + order.totalPrice() + " DETAILS: " + order));
    }

    private void searchCatalog() {
        findByTitle();
        findByAuthor();
        findAndUpdate();
        findByTitle();
        findByAuthor();
    }

    private void initData() {
        catalog.addBook(new CreateBookCommand("Java dla zupełnie początkujących", "Tonny Gaddis", 2019, new BigDecimal ("121.90")));
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
