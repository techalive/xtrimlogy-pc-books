package us.awardspace.tekkno.xtrimlogy.catalog.domain;

import org.springframework.stereotype.Service;
import us.awardspace.tekkno.xtrimlogy.catalog.domain.Book;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CatalogController {
    private final CatalogRepository repository;

    public CatalogController(CatalogRepository repository) {
        this.repository = repository;
    }

    public List<Book> findByTitle(String title) {
        return repository.findAll()
                      .stream()
                      .filter(book -> book.title.startsWith(title))
                      .collect(Collectors.toList());
    }
}
