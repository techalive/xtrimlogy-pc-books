package us.awardspace.tekkno.xtrimlogy;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CatalogService {
    private final Map<Long, Book> storage = new ConcurrentHashMap<>();

    public CatalogService() {
        storage.put(1L, new Book(1L, "Java dla zupełnie początkujących", "Tonny Gaddis", 2019));
        storage.put(2L, new Book(2L, "Czysty kod", "C. Martin", 2018));
        storage.put(3L, new Book(3L, "Kompedium programisty, wyd. X", "Herbert Schildt", 2019));
    }

    List<Book> findByTitle(String title) {
        return storage.values()
                      .stream()
                      .filter(book -> book.title.startsWith(title))
                      .collect(Collectors.toList());
    }
}
