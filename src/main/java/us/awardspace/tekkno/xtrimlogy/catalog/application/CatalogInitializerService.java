package us.awardspace.tekkno.xtrimlogy.catalog.application;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import us.awardspace.tekkno.xtrimlogy.catalog.application.port.CatalogInitializerUseCase;
import us.awardspace.tekkno.xtrimlogy.catalog.application.port.CatalogUseCase;
import us.awardspace.tekkno.xtrimlogy.catalog.application.port.CatalogUseCase.CreateBookCommand;
import us.awardspace.tekkno.xtrimlogy.catalog.db.AuthorJpaRepository;
import us.awardspace.tekkno.xtrimlogy.catalog.domain.Author;
import us.awardspace.tekkno.xtrimlogy.catalog.domain.Book;
import us.awardspace.tekkno.xtrimlogy.jpa.BaseEntity;
import us.awardspace.tekkno.xtrimlogy.order.application.port.ManipulateOrderUseCase;
import us.awardspace.tekkno.xtrimlogy.order.application.port.QueryOrderUseCase;
import us.awardspace.tekkno.xtrimlogy.order.domain.Recipient;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CatalogInitializerService implements CatalogInitializerUseCase {

    private final AuthorJpaRepository authorJpaRepository;
    private final ManipulateOrderUseCase placeOrder;
    private final QueryOrderUseCase queryOrder;
    private final CatalogUseCase catalog;
    private final RestTemplate restTemplate;


    @Override
    @Transactional
    public void initialize() {
        initData();
        placeOrder();
    }

    private void initData() {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("books.csv").getInputStream()))) {
            CsvToBean<CsvBook> build = new CsvToBeanBuilder<CsvBook>(reader)
                    .withType(CsvBook.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            build.stream().forEach(this::initBook);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse CSV file", e);

        }
    }

    private void initBook(CsvBook csvBook)
    {
        Set<Long> authors = Arrays.stream(csvBook.authors.split(","))
                                  .filter(StringUtils::isNotBlank)
                                  .map(String::trim)
                                  .map(this::getOrCreateAuthor)
                                  .map(BaseEntity::getId)
                                  .collect(Collectors.toSet());
        CreateBookCommand command = new CreateBookCommand(
                csvBook.title,
                authors,
                csvBook.year,
                csvBook.amount,
                50L);

        Book book = catalog.addBook(command);
        catalog.updateBookCover(updateBookCoverCommand(book.getId(), csvBook.thumbnail));
    }

    private CatalogUseCase.UpdateBookCoverCommand updateBookCoverCommand(Long bookId, String thumbnailUrl) {
        ResponseEntity<byte[]> response = restTemplate.exchange(thumbnailUrl, HttpMethod.GET, null, byte[].class);
        String contentType = response.getHeaders().getContentType().toString();
        return new CatalogUseCase.UpdateBookCoverCommand(bookId, response.getBody(), contentType, "cover");
    }


    private Author getOrCreateAuthor(String name) {
        return authorJpaRepository
                .findByNameIgnoreCase(name)
                .orElseGet(() -> authorJpaRepository.save(new Author(name)));
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CsvBook {
        @CsvBindByName
        private String title;
        @CsvBindByName
        private String authors;
        @CsvBindByName
        private Integer year;
        @CsvBindByName
        private BigDecimal amount;
        @CsvBindByName
        private String thumbnail;
    }

    private void placeOrder() {
        firstOrder();
        secondOrder();
    }

    private void firstOrder() {
        Book efektywneProgramowanie = catalog.findOneByTitle("Effective Java")
                .orElseThrow(() -> new IllegalStateException("Cannot find a book :-("));
        Book czystyKod = catalog.findOneByTitle("Clean Code")
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
                .item(new ManipulateOrderUseCase.OrderItemCommand(efektywneProgramowanie.getId(), 12))
                .item(new ManipulateOrderUseCase.OrderItemCommand(czystyKod.getId(), 50))
                .build();

        orderSummary(command);
    }

    private void secondOrder() {
        Book javaKompendium = catalog.findOneByTitle("Thinking in Java")
                .orElseThrow(() -> new IllegalStateException("Cannot find a book :-("));
        Book javowcaKompendium = catalog.findOneByTitle("Test-driven Development")
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
                .item(new ManipulateOrderUseCase.OrderItemCommand(javaKompendium.getId(), 9))
                .item(new ManipulateOrderUseCase.OrderItemCommand(javowcaKompendium.getId(), 16))
                .build();

        orderSummary(command);
    }

    private void orderSummary(ManipulateOrderUseCase.PlaceOrderCommand command) {
        ManipulateOrderUseCase.PlaceOrderResponse response = placeOrder.placeOrder(command);

        String result = response.handle(
                orderId -> "Created ORDER with id: " + orderId,
                error -> "Failed to created order: " + error
        );
        log.info(result);

        queryOrder.findAll()
                .forEach(order -> log.info("GOT ORDER WITH TOTAL PRICE: " + order.totalPrice() + " DETAILS: " + order));
    }


}
