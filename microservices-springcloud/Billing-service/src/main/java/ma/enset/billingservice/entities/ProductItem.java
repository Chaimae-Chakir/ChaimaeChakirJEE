package ma.enset.billingservice.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.enset.billingservice.model.Product;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double quantity;
    private double price;
    private  long productID;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToOne
    private Bill bill;
    @Transient
    private Product product;
}
