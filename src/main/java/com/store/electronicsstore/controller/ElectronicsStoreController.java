package com.store.electronicsstore.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.store.electronicsstore.service.ProductService;
import com.store.electronicsstore.service.dto.ProductDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * This controller contains method that helps to manage products for a store.
 *
 */
@RestController
@Api(value = "Electronic store Controller")
public class ElectronicsStoreController {

	private final ProductService productService;

	@Autowired
	public ElectronicsStoreController(ProductService productService) {
		this.productService = productService;
	}

	@ApiOperation(value = "Add new Product")
	@PostMapping("/v1/addNewProduct")
	@ResponseStatus(HttpStatus.CREATED)
	public void addNewProduct(@Valid @RequestBody ProductDto productDto) {
		productService.addNewProduct(productDto);
	}

	@ApiOperation(value = "Update inventory for a given product")
	@PutMapping("/v1/updateInventory/{pid}/{quantityToAdd}")
	@ResponseStatus(HttpStatus.OK)
	public void updateInventory(@PathVariable @NotBlank @NotNull String pid,
			@PathVariable @NotBlank @NotNull @Positive int quantityToAdd) {
		productService.updateQuantity(pid, quantityToAdd);
	}


	@ApiOperation(value = "Get Product details by productId")
	@GetMapping("/v1/product/{pid}")
	public ProductDto getProductById(@PathVariable @NotBlank @NotNull String pid) {

		return productService.getProductById(pid);
	}

	@ApiOperation(value = "Get All Products")
	@GetMapping("/v1/products")
	public List<ProductDto> getAllProducts() {

		return productService.getAllProducts();
	}

	@ApiOperation(value = "Get total inventory of Product by Product Id")
	@GetMapping("/v1/inventory/{pid}")
	public int getNumberOfProductsById(@PathVariable String pid) {

		return productService.getNumberOfProductsById(pid);
	}

	@ApiOperation(value = "Update a product")
	@PutMapping("/v1/updateProduct/{pid}")
	@ResponseStatus(HttpStatus.OK)
	public void updateProduct(@PathVariable String pid, @Valid @RequestBody ProductDto productDto) {
		productService.updateProduct(pid, productDto);
	}
	
	@ApiOperation(value = "Delete a product")
	@DeleteMapping("/v1/product/{pid}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeProduct(@PathVariable @NotBlank @NotNull String pid) {
		productService.removeProduct(pid);
	}

}
