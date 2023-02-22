package com.glc.alisnobbacartservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.glc.alisnobbacartservice.Model.CartModel;

public interface ICartRepository extends JpaRepository<CartModel, Long>{
    
}
