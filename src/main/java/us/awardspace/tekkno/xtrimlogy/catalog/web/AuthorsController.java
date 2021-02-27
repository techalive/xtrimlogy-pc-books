package us.awardspace.tekkno.xtrimlogy.catalog.web;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.awardspace.tekkno.xtrimlogy.catalog.application.port.AuthorUseCase;
import us.awardspace.tekkno.xtrimlogy.catalog.domain.Author;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/authors")
public class AuthorsController {
    private final AuthorUseCase authors;

    @GetMapping
    public List<Author> findAll() {
        return authors.findAll();
    }
}
