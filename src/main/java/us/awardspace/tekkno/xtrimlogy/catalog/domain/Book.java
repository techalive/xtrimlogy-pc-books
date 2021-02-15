package us.awardspace.tekkno.xtrimlogy.catalog.domain;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Book {
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
