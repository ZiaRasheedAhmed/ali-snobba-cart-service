package com.glc.alisnobbacartservice.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.glc.alisnobbacartservice.Model.CartModel;
import com.glc.alisnobbacartservice.Repository.ICartRepository;

@RestController
@RequestMapping("/cart")
@CrossOrigin("*")
public class CartController {
    @Autowired
    private ICartRepository cartRepository;

    @GetMapping("/allcart")
    public List<CartModel> getAllCartData(){
        return cartRepository.findAll();
    }
    @PostMapping("/addcart")
    public String addToCart(@RequestBody CartModel cartModel){
        cartRepository.save(cartModel);
        return "Cart Added "+cartModel.getId();
    }
    @GetMapping("/{id}")
    public CartModel getById(@PathVariable Long id){
        return cartRepository.findById(id).orElse(null);
    } 
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        cartRepository.deleteById(id);
    }
    @DeleteMapping("/deleteall")
    public void deleteAll(){
         cartRepository.deleteAll();
    }
}
