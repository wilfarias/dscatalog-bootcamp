package com.springproject.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springproject.dscatalog.dto.CategoryDTO;
import com.springproject.dscatalog.dto.ProductDTO;
import com.springproject.dscatalog.entities.Category;
import com.springproject.dscatalog.entities.Product;
import com.springproject.dscatalog.repositories.CategoryRepository;
import com.springproject.dscatalog.repositories.ProductRepository;
import com.springproject.dscatalog.services.exceptions.DataBaseException;
import com.springproject.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired //injecao de dependencia automatica do Spring
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAll(PageRequest pageRequest){
		Page<Product> productPaged = repository.findAll(pageRequest);
		/* Para cada elemento do stream mapeado (que são tipo Product),
		 * passa-o para o construtor de ProductDTO */
		return productPaged.map(product -> new ProductDTO(product, product.getCategories()));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product productById = obj.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
		return new ProductDTO(productById, productById.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO productDto) {
		 Product entityProduct = new Product();
		 copyProductDtoToEntity(productDto, entityProduct);		 
		 entityProduct = repository.save(entityProduct);//o método save, por padrão, retorna a entidade salva ao final da transação
		 return new ProductDTO(entityProduct, entityProduct.getCategories());
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO productDto) {
		try {
			Product entityProduct = repository.getReferenceById(id);
			copyProductDtoToEntity(productDto, entityProduct);
			entityProduct = repository.save(entityProduct);
			return new ProductDTO(entityProduct);
		} catch (EntityNotFoundException e) {//Ao disparar a exeção EntityNotFound, lança a exceção personalizada ResourceNotFound	
			throw new ResourceNotFoundException("Id"+ id +" not found ");
		}
	}

	public void delete(Long id) {
		if(!repository.existsById(id))
			throw new ResourceNotFoundException("Produto não encontrado");
		
		try{
			repository.deleteById(id);
		} catch(DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity violation");
		}
	}
	
	private void copyProductDtoToEntity (ProductDTO productDto, Product entity) {
		entity.setName(productDto.getName());
		entity.setDescription(productDto.getDescription());
		entity.setPrice(productDto.getPrice());
		entity.setImgUrl(productDto.getImgUrl());
		entity.setDate(productDto.getDate());
		
		entity.getCategories().clear();
		for(CategoryDTO categoryDto : productDto.getCategoriesDto()) {
			Category category = categoryRepository.getReferenceById(categoryDto.getId());
			entity.getCategories().add(category);
		}		
	}
}
