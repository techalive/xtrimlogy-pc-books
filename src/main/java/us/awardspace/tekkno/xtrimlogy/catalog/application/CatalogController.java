package us.awardspace.tekkno.xtrimlogy.catalog.application;

import org.springframework.stereotype.Controller;
import us.awardspace.tekkno.xtrimlogy.catalog.domain.Book;

import java.util.List;

@Controller
public class CatalogController {
    private final CatalogController service;

    public CatalogController(CatalogController service) {
        this.service = service;
    }

    public List<Book> findByTitle(String title) {
        return service.findByTitle(title);
    }
}
