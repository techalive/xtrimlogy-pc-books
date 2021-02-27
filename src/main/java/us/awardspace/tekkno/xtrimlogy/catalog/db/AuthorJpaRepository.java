package us.awardspace.tekkno.xtrimlogy.catalog.db;

import org.springframework.data.jpa.repository.JpaRepository;
import us.awardspace.tekkno.xtrimlogy.catalog.domain.Author;

public interface AuthorJpaRepository extends JpaRepository<Author, Long> {
}
