package com.springproject.dscatalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springproject.dscatalog.dto.ProductDTO;
import com.springproject.dscatalog.factories.Factory;
import com.springproject.dscatalog.services.ProductService;
import com.springproject.dscatalog.services.exceptions.DataBaseException;
import com.springproject.dscatalog.services.exceptions.ResourceNotFoundException;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService service;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private PageImpl<ProductDTO> page;
	private ProductDTO productDTO;
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	
	@BeforeEach
	void setUp() throws Exception {
		
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(productDTO));
		
		Mockito.when(service.findAll(ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(service.findById(existingId)).thenReturn(productDTO);
		Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		Mockito.when(service.insert(any())).thenReturn(productDTO);
		
		Mockito.when(service.update(eq(existingId), any())).thenReturn(productDTO);
		Mockito.when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
		
		Mockito.doNothing().when(service).delete(existingId);
		Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		Mockito.doThrow(DataBaseException.class).when(service).delete(dependentId);
		
	}
	
	@Test
	public void insertShouldReturnCreatedWhenNewProductIsCreated() throws Exception {
		
		String productDtoJSON = objectMapper.writeValueAsString(productDTO);
		
		mockMvc.perform(post("/products")
				.content(productDtoJSON)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated());
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		mockMvc.perform(delete("/products/{id}", nonExistingId))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() throws Exception {
		mockMvc.perform(delete("/products/{id}", existingId))
		.andExpect(status().isNoContent());
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		
		String productDtoJSON = objectMapper.writeValueAsString(productDTO);
		
		mockMvc.perform(put("/products/{id}", nonExistingId)
				.content(productDtoJSON) // corpo da requisicao
				.contentType(MediaType.APPLICATION_JSON) // tipo de dado enviado
				.accept(MediaType.APPLICATION_JSON)) // tipo de retorno esperado
		
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void updateShouldReturnAProductDtoWhenIdExists() throws Exception {
		
		String productDtoJSON = objectMapper.writeValueAsString(productDTO);
		
		mockMvc.perform(put("/products/{id}", existingId)
				.content(productDtoJSON) // corpo da requisicao
				.contentType(MediaType.APPLICATION_JSON) // tipo de dado enviado
				.accept(MediaType.APPLICATION_JSON)) // tipo de retorno esperado
		
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").exists())
		.andExpect(jsonPath("$.name").exists());		
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		
		mockMvc.perform(get("/products/{id}", nonExistingId))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void findByIdShouldReturnAProductDtoWhenIdExists() throws Exception {
		
		mockMvc.perform(get("/products/{id}", existingId))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").exists())
		.andExpect(jsonPath("$.name").exists());		
	}
	
	@Test
	public void findAllShouldReturnAPage() throws Exception {
		
		mockMvc.perform(get("/products")).andExpect(status().isOk());
	}

}
