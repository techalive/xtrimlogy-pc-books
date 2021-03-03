package us.awardspace.tekkno.xtrimlogy.catalog.web;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.awardspace.tekkno.xtrimlogy.catalog.application.port.CatalogUseCase;
import us.awardspace.tekkno.xtrimlogy.catalog.db.AuthorJpaRepository;
import us.awardspace.tekkno.xtrimlogy.catalog.domain.Author;
import us.awardspace.tekkno.xtrimlogy.catalog.domain.Book;
import us.awardspace.tekkno.xtrimlogy.order.application.port.ManipulateOrderUseCase;
import us.awardspace.tekkno.xtrimlogy.order.application.port.QueryOrderUseCase;
import us.awardspace.tekkno.xtrimlogy.order.domain.OrderItem;
import us.awardspace.tekkno.xtrimlogy.order.domain.Recipient;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Set;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final CatalogUseCase catalog;
    private final ManipulateOrderUseCase placeOrder;
    private final QueryOrderUseCase queryOrder;
    private final AuthorJpaRepository authorRepository;

    @PostMapping("/data")
    @Transactional
    public void initialize() {
        initData();
        // searchCatalog();
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

        ManipulateOrderUseCase.PlaceOrderCommand command = ManipulateOrderUseCase.PlaceOrderCommand
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
        Book javowcaKompendium = catalog.findOneByTitle("Efektywne programowanie")
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

        ManipulateOrderUseCase.PlaceOrderCommand command = ManipulateOrderUseCase.PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .item(new OrderItem(javaKompendium.getId(), 9))
                .item(new OrderItem(javowcaKompendium.getId(), 16))
                .build();

        orderSummary(command);
    }

    private void orderSummary(ManipulateOrderUseCase.PlaceOrderCommand command) {
        ManipulateOrderUseCase.PlaceOrderResponse response = placeOrder.placeOrder(command);

        String result = response.handle(
                orderId -> "Created ORDER with id: " + orderId,
                error -> "Failed to created order: " + error
        );
        System.out.println(result);

        queryOrder.findAll()
                .forEach(order -> System.out.println("GOT ORDER WITH TOTAL PRICE: " + order.totalPrice() + " DETAILS: " + order));
    }

/*    private void searchCatalog() {
        findByTitle();
        findByAuthor();
        findAndUpdate();
        findByTitle();
        findByAuthor();
    }*/

    private void initData() {
        Author gaddis = new Author("Tonny", "Gaddis");
        Author martin = new Author("C", "Martin");
        Author schildt = new Author("Herbert", "Schildt");
        Author bloch = new Author("Joshua", "Bloch");
        authorRepository.save(gaddis);
        authorRepository.save(martin);
        authorRepository.save(schildt);
        authorRepository.save(bloch);


        catalog.addBook(new CatalogUseCase.CreateBookCommand("Java dla zupełnie początkujących", Set.of(gaddis.getId()), 2019, new BigDecimal("121.90")));
        catalog.addBook(new CatalogUseCase.CreateBookCommand("Czysty kod", Set.of(martin.getId()), 2018, new BigDecimal ("29.90")));
        catalog.addBook(new CatalogUseCase.CreateBookCommand("Kompedium programisty, wyd. X", Set.of(schildt.getId()), 2019, new BigDecimal ("49.90")));
        catalog.addBook(new CatalogUseCase.CreateBookCommand("Kompedium javowca, wyd. X", Set.of(schildt.getId()), 2019, new BigDecimal ("48.90")));
        catalog.addBook(new CatalogUseCase.CreateBookCommand("Efektywne programowanie", Set.of(bloch.getId()), 2019, new BigDecimal ("47.90")));
    }

/*    private void findByAuthor() {
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
    }*/

}
