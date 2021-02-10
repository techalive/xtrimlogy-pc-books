package us.awardspace.tekkno.xtrimlogy.catalog.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.awardspace.tekkno.xtrimlogy.catalog.application.port.CatalogUseCase;
import us.awardspace.tekkno.xtrimlogy.catalog.domain.Book;
import us.awardspace.tekkno.xtrimlogy.catalog.domain.CatalogRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class CatalogService implements CatalogUseCase {
    private final CatalogRepository repository;

    @Override
    public List<Book> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Book> findByTitle(String title) {
        return repository.findAll()
                         .stream()
                         .filter(book -> book.getTitle().contains(title))
                         .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findOneByTitle(String title) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().contains(title))
                .findFirst();
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return repository.findAll()
                         .stream()
                         .filter(book -> book.getAuthor().startsWith(author))
                         .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findOneByTitleAndAuthor(String title, String author) {
     return  repository.findAll()
                  .stream()
                  .filter(book -> book.getTitle().contains(title))
                  .filter(book -> book.getAuthor().startsWith(author))
                  .findFirst();
    }

    @Override
    public void addBook(CreateBookCommand command) {
        Book book = command.toBook();
        repository.save(book);
    }

    @Override
    public UpdateBookResponse updateBook(UpdateBookCommand command) {
      return   repository
                .findById(command.getId())
                .map(book -> {
                    Book updatedBook = command.updateFields(book);
                    repository.save(updatedBook);
                    return UpdateBookResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateBookResponse(false, Collections.singletonList("Book not found with ID: " + command.getId())));
    }

    @Override
    public void removeById(Long id) {
        repository.removeById(id);
    }

}
