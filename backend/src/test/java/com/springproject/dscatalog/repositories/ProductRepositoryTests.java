package com.springproject.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.springproject.dscatalog.entities.Product;
import com.springproject.dscatalog.factories.FactoryProduct;
import com.springproject.dscatalog.services.exceptions.ResourceNotFoundException;

@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalIdProducts;
	private Product product;
	
	@BeforeEach
	void setUp() throws Exception{
		product = FactoryProduct.createProduct();
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalIdProducts = 25L;
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {				
		repository.deleteById(existingId);		
		Optional<Product> result = repository.findById(existingId);		
		Assertions.assertFalse(result.isPresent());			
	}
	
	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		product.setId(null);
		product = repository.save(product);
		
		Assertions.assertNotNull(product);
		Assertions.assertEquals(countTotalIdProducts + 1, product.getId());
	}
	
	@Test
	public void findByIdShouldReturnOptionalNotNullWhenIdExists() {
		Optional<Product> result = repository.findById(existingId);
		
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void findByIdShouldReturnOptionalEmptyWhenIdDoesNotExists() {
		Optional<Product> result = repository.findById(nonExistingId);
		
		Assertions.assertEquals(Optional.empty(),result);
	}

}
