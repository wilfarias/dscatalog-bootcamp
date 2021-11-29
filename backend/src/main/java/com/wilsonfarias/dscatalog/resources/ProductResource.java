package com.wilsonfarias.dscatalog.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.wilsonfarias.dscatalog.dto.ProductDTO;
import com.wilsonfarias.dscatalog.services.ProductService;


@RestController
@RequestMapping(value = "/products")
public class ProductResource {
	
	@Autowired
	private ProductService service;
	
	
	@GetMapping //Define o endpoint
	public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable){		
		Page<ProductDTO> list = service.findAllPaged(pageable);		
		return ResponseEntity.ok().body(list);
	}

	/* @PathVariable linka a variável utilizada no parâmetro do método com o id do endpoint*/	
	
	@GetMapping(value = "/{id}") //Define o endpoint
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
		ProductDTO dto = service.findById(id);		
		return ResponseEntity.ok().body(dto);
	}
	
	// Para se inserir um novo recurso, pelo padrão REST se utiliza um POST
	// @RequestBody, linka o objeto da requisição com o objeto dto do método
	
	@PostMapping
	public ResponseEntity<ProductDTO> insertProduct(@RequestBody ProductDTO dto){
		dto = service.insert(dto);
		//Insere no cabeçalho da resposta HTTP o location do novo registro criado
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO dto){
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
