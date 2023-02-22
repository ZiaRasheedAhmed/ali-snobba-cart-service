package com.glc.alisnobbacartservice.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart-data")
@Getter
public class CartModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private Long productID;
    @Setter
    private Long quantity;
    @Setter
    private Long totalPrice;

    public CartModel(){}

    public CartModel(Long productID, Long quantity, Long totalPrice) {
        this.productID = productID;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
    
}
