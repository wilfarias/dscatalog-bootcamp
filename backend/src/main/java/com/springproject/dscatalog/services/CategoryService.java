package com.springproject.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springproject.dscatalog.dto.CategoryDTO;
import com.springproject.dscatalog.entities.Category;
import com.springproject.dscatalog.repositories.CategoryRepository;
import com.springproject.dscatalog.services.exceptions.DataBaseException;
import com.springproject.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

	@Autowired //injecao de dependencia automatica do Spring
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAll(Pageable pageable){
		Page<Category> categoryPaged = repository.findAll(pageable);
		/* Para cada elemento do stream mapeado (que são tipo Category),
		 * passa-o para o construtor de CategoryDTO */
		return categoryPaged.map(categoryObj -> new CategoryDTO(categoryObj));
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		Category categoryById = obj.orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
		return new CategoryDTO(categoryById);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		 Category category = new Category();
		 category.setName(dto.getName());
		 /*o método save, por padrão, retorna a entidade salva
		  * ao final da transação*/
		 category = repository.save(category);
		 return new CategoryDTO(category);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
			Category category = repository.getReferenceById(id);
			category.setName(dto.getName());
			category = repository.save(category);
			return new CategoryDTO(category);
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
