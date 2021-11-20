package com.wilsonfarias.dscatalog.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

	/* @PathVariable linka a variável utilizada no parâmetro do método com o id do endpoint*/	
	
	@GetMapping(value = "/{id}") //Define o endpoint
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
		CategoryDTO dto = service.findById(id);		
		return ResponseEntity.ok().body(dto);
	}
	
	// Para se inserir um novo recurso, pelo padrão REST se utiliza um POST
	// @RequestBody, linka o objeto da requisição com o objeto dto do método
	
	@PostMapping
	public ResponseEntity<CategoryDTO> insertCategory(@RequestBody CategoryDTO dto){
		dto = service.insert(dto);
		//Insere no cabeçalho da resposta HTTP o location do novo registro criado
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
}
