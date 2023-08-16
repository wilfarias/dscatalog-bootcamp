package com.springproject.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.springproject.dscatalog.dto.ProductDTO;
import com.springproject.dscatalog.repositories.ProductRepository;
import com.springproject.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional //ao final de cada test o BD executa um rollback ao estado inicial
public class ProductServiceIntegrationTests {
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception{
		
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
	}
	
	@Test
	public void findAllShouldReturnPage() {
		
		PageRequest page = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAll(page);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(10, result.getSize());
		
	}
	
	@Test
	public void findAllShouldReturnSortedPageWhenSortByName() {
		
		PageRequest page = PageRequest.of(0, 10, Sort.by("name"));
		
		Page<ProductDTO> result = service.findAll(page);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
		
	}
	
	@Test
	public void findAllShouldReturnEmptyPageWhenPageDoesNotExist() {
		
		PageRequest page = PageRequest.of(50, 10);
		
		Page<ProductDTO> result = service.findAll(page);
		
		Assertions.assertTrue(result.isEmpty());
		
	}
	
	@Test
	public void deleteShouldDeleteProductWhenIdExists() {
		
		service.delete(existingId);		
		Assertions.assertEquals(countTotalProducts - 1, repository.count());
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
	}

}
