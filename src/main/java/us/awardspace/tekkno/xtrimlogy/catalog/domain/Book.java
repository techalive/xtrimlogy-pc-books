package us.awardspace.tekkno.xtrimlogy.catalog.domain;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Book {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   private String title;
   private String author;
   private Integer year;
   private BigDecimal price;
   private String coverId;

   public Book(String title, String author, Integer year, BigDecimal price) {
      this.title = title;
      this.author = author;
      this.year = year;
      this.price = price;
   }
}
