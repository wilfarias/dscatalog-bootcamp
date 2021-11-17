package com.wilsonfarias.dscatalog.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wilsonfarias.dscatalog.entities.Category;

/* O resource implementa o controlador REST do modelo de camadas,
 * ou seja, a API REST */

@RestController
@RequestMapping(value = "/categories") //rota do recurso REST
public class CategoryResource {
	
	/* Response Entity encapsula as respostas HTTP utilizada no REST;
	 * Ele é um Generic, dessa forma, pode-se definir o tipo de resposta esperada;
	 * No caso ele retorna a lista com as categorias no corpo da resposta HTTP;*/
	
	@GetMapping //Define o endpoint
	public ResponseEntity<List<Category>> findAll(){
		List<Category> list = new ArrayList<>();
		list.add(new Category(1L, "Books"));
		list.add(new Category(2L, "Electronics"));
		return ResponseEntity.ok().body(list);
	}

}
