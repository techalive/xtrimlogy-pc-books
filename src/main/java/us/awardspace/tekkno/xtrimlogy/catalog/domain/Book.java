package us.awardspace.tekkno.xtrimlogy.catalog.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import us.awardspace.tekkno.xtrimlogy.jpa.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString(exclude = "authors")
@RequiredArgsConstructor
@Entity
public class Book extends BaseEntity {
   @Column(unique = true)
   private String title;
   private Integer year;
   private BigDecimal price;
   private Long coverId;
   private Long available;

   @JsonIgnoreProperties("books")
   @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinTable
   private Set<Author> authors = new HashSet<>();

   public Book(String title, Integer year, BigDecimal price, Long available) {
      this.title = title;
      this.year = year;
      this.price = price;
      this.available = available;
   }

   public void addAuthor(Author author) {
      authors.add(author);
      author.getBooks().add(this);
   }

   public void removeAuthor(Author author) {
      authors.remove(author);
      author.getBooks().remove(this);
   }

   public void removeAuthors() {
      Book self = this;
      authors.forEach(author -> author.getBooks().remove(self));
      authors.clear();
   }
}
