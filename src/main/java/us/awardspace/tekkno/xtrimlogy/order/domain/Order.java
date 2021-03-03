package us.awardspace.tekkno.xtrimlogy.order.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import us.awardspace.tekkno.xtrimlogy.jpa.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order extends BaseEntity {

   @Builder.Default
   @Enumerated(value = EnumType.STRING)
   private OrderStatus status = OrderStatus.NEW;

   @OneToMany(cascade = CascadeType.ALL)
   @JoinColumn(name = "order_id")
   private List<OrderItem> items;

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   private Recipient recipient;
   @CreatedDate
   private LocalDateTime createdAt;
   @LastModifiedDate
   private LocalDateTime updatedAt;

}
