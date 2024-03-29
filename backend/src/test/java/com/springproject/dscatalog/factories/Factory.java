package com.springproject.dscatalog.factories;

import java.time.Instant;

import com.springproject.dscatalog.dto.ProductDTO;
import com.springproject.dscatalog.entities.Category;
import com.springproject.dscatalog.entities.Product;

public class Factory {
	
	public static Product createProduct() {
		Product prod = new Product(1L,"Phone","Good Phone",800.0,null,Instant.parse("2020-10-20T03:00:00Z"));
		prod.getCategories().add(createCategory());
		return prod;
	}
	
	public static ProductDTO createProductDTO() {
		Product prod = createProduct();
		return new ProductDTO(prod, prod.getCategories());
	}
	
	public static Category createCategory() {
		return new Category(1L, "Electronics");
	}

}
