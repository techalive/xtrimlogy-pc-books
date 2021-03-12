package us.awardspace.tekkno.xtrimlogy.order.domain;

import lombok.*;
import us.awardspace.tekkno.xtrimlogy.jpa.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Recipient extends BaseEntity {
   private String email;
   private String name;
   private String phone;
   private String street;
   private String city;
   private String zipCode;
}
