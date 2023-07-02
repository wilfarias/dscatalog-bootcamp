package com.springproject.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springproject.dscatalog.dto.ProductDTO;
import com.springproject.dscatalog.entities.Product;
import com.springproject.dscatalog.repositories.ProductRepository;
import com.springproject.dscatalog.services.exceptions.DataBaseException;
import com.springproject.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired //injecao de dependencia automatica do Spring
	private ProductRepository repository;
	
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
		Product productById = obj.orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
		return new ProductDTO(productById, productById.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		 Product product = new Product();
		 product.setName(dto.getName());
		 /*o método save, por padrão, retorna a entidade salva
		  * ao final da transação*/
		 product = repository.save(product);
		 return new ProductDTO(product);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product product = repository.getReferenceById(id);
			product.setName(dto.getName());
			product = repository.save(product);
			return new ProductDTO(product);
		} catch (EntityNotFoundException e) {
			/* Ao disparar a exeção EntityNotFound,
			 * lança a exceção personalizada ResourceNotFound */
			throw new ResourceNotFoundException("Id not found "+ id);
		}
	}

	public void delete(Long id) {
		if(!repository.existsById(id))
			throw new ResourceNotFoundException("Categoria não encontrada");
		
		try{
			repository.deleteById(id);
		} catch(DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity violation");
		}
	}
}
