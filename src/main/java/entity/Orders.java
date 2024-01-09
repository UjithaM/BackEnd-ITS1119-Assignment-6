package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    @Id
    private String orderId;
    private String date;
    private String customerId;
    private Double netTotal;
    private Integer discount;
    private Double cash;
    private Double subTotal;
}
