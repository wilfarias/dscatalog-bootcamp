package com.wilsonfarias.dscatalog.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wilsonfarias.dscatalog.dto.CategoryDTO;
import com.wilsonfarias.dscatalog.services.CategoryService;

/* O resource implementa o controlador REST do modelo de camadas,
 * ou seja, a API REST */

@RestController
@RequestMapping(value = "/categories") //rota do recurso REST
public class CategoryResource {
	
	@Autowired
	private CategoryService service;
	
	/* Response Entity encapsula as respostas HTTP utilizada no REST;
	 * Ele é um Generic, dessa forma, pode-se definir o tipo de resposta esperada;
	 * No caso ele retorna a lista com as categorias no corpo da resposta HTTP;*/
	
	@GetMapping //Define o endpoint
	public ResponseEntity<List<CategoryDTO>> findAll(){
		List<CategoryDTO> list = service.findAll();		
		return ResponseEntity.ok().body(list);
	}

	/*PathVariable linka a variável utilizada no parâmetro do método com o id do endpoint*/	
	@GetMapping(value = "/{id}") //Define o endpoint
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
		CategoryDTO dto = service.findById(id);		
		return ResponseEntity.ok().body(dto);
	}
}
