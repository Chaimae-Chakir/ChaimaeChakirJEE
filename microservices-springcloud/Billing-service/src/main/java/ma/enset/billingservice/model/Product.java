package ma.enset.billingservice.model;

import lombok.Data;

@Data
public class Product {
    private Long id;
    private double name;
    private double quantity;
    private double price;
}
