package us.awardspace.tekkno.xtrimlogy.order.domain;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recipient {
   private String name;
   private String phone;
   private String street;
   private String city;
   private String zipCode;
   private String email;
}
