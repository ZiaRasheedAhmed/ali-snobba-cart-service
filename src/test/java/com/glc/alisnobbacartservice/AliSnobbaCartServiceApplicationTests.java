package com.glc.alisnobbacartservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glc.alisnobbacartservice.Controller.CartController;
import com.glc.alisnobbacartservice.Model.CartModel;
import com.glc.alisnobbacartservice.Repository.ICartRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// import org.junit.jupiter.api.BeforeEach;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
class AliSnobbaCartServiceApplicationTests {

	@Mock
	private ICartRepository cartRepository;
	@InjectMocks
	private CartController cartController;
	private MockMvc mvc;
	private JacksonTester<List<CartModel>> jsonCarts;
	private JacksonTester<CartModel> jsonCart;

	@BeforeEach
	public void setup() {
		JacksonTester.initFields(this, new ObjectMapper());
		mvc = MockMvcBuilders.standaloneSetup(cartController).build();
	}

	@Test
	public void testCartID() {
		CartModel cartModel = new CartModel();
		Long id = 1L;
		cartModel.setId(id);
		assertEquals(id, cartModel.getId());
	}

	@Test
	public void builderCartModel() {
		Long id = 1L;
		Long productID = 1L;
		Long quantity = 3L;
		Long totalPrice = 684750000L;
		CartModel cartModel = CartModel.builder()
				.id(id)
				.productID(productID)
				.quantity(quantity)
				.totalPrice(totalPrice)
				.build();
		assertEquals(id, cartModel.getId());
		assertEquals(productID, cartModel.getProductID());
		assertEquals(quantity, cartModel.getQuantity());
		assertEquals(totalPrice, cartModel.getTotalPrice());
	}

	@Test
	public void canGetAndSetCartData() throws Exception {
		Long productID = 1L;
		Long quantity = 10L;
		Long totalPrice = 12012000L;
		CartModel cartModel = new CartModel(productID, quantity, totalPrice);
		assertEquals(productID, cartModel.getProductID());
		assertEquals(quantity, cartModel.getQuantity());
		assertEquals(totalPrice, cartModel.getTotalPrice());
	}

	@Test
	public void canGetAllcarts() throws Exception {
		CartModel cartModel1 = new CartModel(1L, 5L, 6L, 12012000L);
		CartModel cartModel2 = new CartModel(2L, 3L, 2L, 12012000L);
		List<CartModel> cartModelsList = new LinkedList<>();
		cartModelsList.add(cartModel1);
		cartModelsList.add(cartModel2);
		when(cartRepository.findAll()).thenReturn(cartModelsList);
		mvc.perform(get("/cart/allcart")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(jsonCarts.write(cartModelsList).getJson()));
	}

	@Test
	public void canGetCartById() throws Exception {
		CartModel cartModel = new CartModel(1L, 4L, 3L, 12012000L);
		when(cartRepository.findById(1L))
				.thenReturn(Optional.of(cartModel));
		mvc.perform(get("/cart/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonCart.write(cartModel).getJson()));
	}

	@Test
	public void canPostCartData() throws Exception {
		CartModel cartModel = new CartModel(1L, 4L, 3L, 12012000L);
		mvc.perform(post("/cart/addcart")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonCart.write(cartModel).getJson()))
				.andExpect(status().isOk());
	}

	@Test
	public void canDeleteAllCart() throws Exception {
		mvc.perform(delete("/cart/deleteall")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void canDeleteCartById() throws Exception {
		Long id = 1L;
		if (cartRepository.findById(id).isPresent()) {
			willDoNothing().given(cartRepository).deleteById(id);
			ResultActions response = mvc.perform(delete("/cart/{id}", id));
			response.andExpect(status().isOk()).andDo(print());
			verify(cartRepository, times(1)).deleteById(id);
		} else {
			cartRepository.findById(id).isEmpty();
		}
	}
	@Test
	public void  canDeleteACartWhenTheCartDoesntExist() throws Exception{
		Long id = 1L;
		doReturn(Optional.empty()).when(cartRepository).findById(id);
		mvc.perform(MockMvcRequestBuilders.delete("/cart/{id}", id)).andExpect(status().isNotFound());
	}
}
