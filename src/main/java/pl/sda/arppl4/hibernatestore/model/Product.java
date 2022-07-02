package pl.sda.arppl4.hibernatestore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;
    private String producent;
    private LocalDate expiryDate;
    private Double quantity;
    private ProductUnit productUnit;


    public Product(Object o, String name, Double price, String producent, LocalDate expiryDate, Double quantity, ProductUnit productUnit) {
        this.name = name;
        this.price = price;
        this.producent = producent;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.productUnit = productUnit;
    }
}
