package us.awardspace.tekkno.xtrimlogy.catalog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import us.awardspace.tekkno.xtrimlogy.catalog.application.port.CatalogUseCase;
import us.awardspace.tekkno.xtrimlogy.catalog.db.AuthorJpaRepository;
import us.awardspace.tekkno.xtrimlogy.catalog.domain.Author;
import us.awardspace.tekkno.xtrimlogy.catalog.domain.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.*;
import static us.awardspace.tekkno.xtrimlogy.catalog.application.port.CatalogUseCase.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class CatalogControllerIT {
    @Autowired
    AuthorJpaRepository authorJpaRepository;
    @Autowired
    CatalogUseCase catalogUseCase;
    @Autowired
    CatalogController controller;


    @Test
    public void getAllBooks() {

        givenEffectiveJava();
        givenJavaConcurrencyInPractice();

        List<Book> all = controller.getAll(Optional.empty(), Optional.empty());


        assertEquals(2, all.size());
    }

    @Test
    public void getBooksByAuthor() {

        givenEffectiveJava();
        givenJavaConcurrencyInPractice();

        List<Book> all = controller.getAll(Optional.empty(), Optional.of("Bloch"));


        assertEquals(1, all.size());
        assertEquals("Effective Java", all.get(0).getTitle());
    }


    @Test
    public void getBooksByTitle() {

        givenEffectiveJava();
        givenJavaConcurrencyInPractice();

        List<Book> all = controller.getAll(Optional.of("Java Concurrency in Practice"), Optional.empty());


        assertEquals(1, all.size());
        assertEquals("Java Concurrency in Practice", all.get(0).getTitle());
    }


    private void givenEffectiveJava() {
        Author bloch = authorJpaRepository.save(new Author("Joshua Bloch"));
        catalogUseCase.addBook(new CreateBookCommand(
                "Effective Java",
                Set.of(bloch.getId()),
                2005,
                new BigDecimal("99.90"),
                50L
        ));
    }

    private void givenJavaConcurrencyInPractice() {
        Author goetz = authorJpaRepository.save(new Author("Brian Goetz"));
        catalogUseCase.addBook(new CreateBookCommand(
                "Java Concurrency in Practice",
                Set.of(goetz.getId()),
                2006,
                new BigDecimal("129.90"),
                50L
        ));
    }
}