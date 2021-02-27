package us.awardspace.tekkno.xtrimlogy.catalog.application.port;

import us.awardspace.tekkno.xtrimlogy.catalog.domain.Author;

import java.util.List;

public interface AuthorUseCase {
    List<Author> findAll();
}
