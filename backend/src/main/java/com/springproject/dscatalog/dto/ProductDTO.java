package com.springproject.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springproject.dscatalog.entities.Category;
import com.springproject.dscatalog.entities.Product;

public class ProductDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;	
	private String name;
	private String description;	
	private Double price;
	private String imgUrl;
	private Instant date;
	
	List<CategoryDTO> categoriesDto = new ArrayList<>();
	
	public ProductDTO() {}

	public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant date) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
		this.date = date;
	}

	public ProductDTO( Product entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.description = entity.getDescription();
		this.price = entity.getPrice();
		this.imgUrl = entity.getImgUrl();
		this.date = entity.getDate();
	}
	
	public ProductDTO( Product entity, Set<Category> categories) {
		this(entity); //referencia o construtor acima, com entity como parâmetro
		categories.forEach(category -> this.categoriesDto.add(new CategoryDTO(category)));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	/* Utiliza-se essa propriedade para personalizar o JSON key
	 * a ser enviado para o front-end, assim ele não fica
	 * com o nome categoriesDTO, mas apenas categories*/
	@JsonProperty("categories") 
	public List<CategoryDTO> getCategoriesDto() {
		return categoriesDto;
	}

	/*public void setCategoriesDto(List<CategoryDTO> categories) {
		this.categoriesDto = categories;
	}*/
	
	
}
