package us.awardspace.tekkno.xtrimlogy.order.domain;

import lombok.*;
import us.awardspace.tekkno.xtrimlogy.jpa.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem extends BaseEntity {
    private Long bookId;
    private int quantity;
}
