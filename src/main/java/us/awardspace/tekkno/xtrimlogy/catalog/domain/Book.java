package us.awardspace.tekkno.xtrimlogy.catalog.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = "authors")
@RequiredArgsConstructor
@Entity
public class Book {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   private String title;
   private Integer year;
   private BigDecimal price;
   private Long coverId;

   @JsonIgnoreProperties("books")
   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable
   private Set<Author> authors;

   public Book(String title, Integer year, BigDecimal price) {
      this.title = title;
      this.year = year;
      this.price = price;
   }
}
