package com.springproject.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springproject.dscatalog.dto.CategoryDTO;
import com.springproject.dscatalog.entities.Category;
import com.springproject.dscatalog.repositories.CategoryRepository;
import com.springproject.dscatalog.services.exceptions.EntityNotFoundException;



@Service
public class CategoryService {

	@Autowired //injecao de dependencia automatica do Spring
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		List<Category> list = repository.findAll();
		/* Para cada elemento do stream mapeado (que são tipo Category),
		 * passa-o para o construtor de CategoryDTO
		 * e converte o strem novamente para um List ao final
		 */
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		Category categoryById = obj.orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
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
}
