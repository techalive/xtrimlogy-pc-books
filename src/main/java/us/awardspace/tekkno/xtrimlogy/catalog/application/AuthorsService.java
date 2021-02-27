package us.awardspace.tekkno.xtrimlogy.catalog.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import us.awardspace.tekkno.xtrimlogy.catalog.application.port.AuthorUseCase;
import us.awardspace.tekkno.xtrimlogy.catalog.db.AuthorJpaRepository;
import us.awardspace.tekkno.xtrimlogy.catalog.domain.Author;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthorsService implements AuthorUseCase {
    private final AuthorJpaRepository repository;

    @Override
    public List<Author> findAll() {
        return repository.findAll();
    }
}
