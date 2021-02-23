package us.awardspace.tekkno.xtrimlogy.catalog.db;

import org.springframework.data.jpa.repository.JpaRepository;
import us.awardspace.tekkno.xtrimlogy.catalog.domain.Book;

public interface BookJpaRepository extends JpaRepository<Book, Long> {
}
