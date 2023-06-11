package com.springproject.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springproject.dscatalog.entities.Category;
import com.springproject.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired //injecao de dependencia automatica do Spring
	private CategoryRepository repository;
	
	public List<Category> findAll(){
		List<Category> list = repository.findAll();
		return list;
	}
}
