package com.store.electronicsstore.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.electronicsstore.domain.Product;
import com.store.electronicsstore.domain.ProductRepository;
import com.store.electronicsstore.service.dto.ProductDto;
import com.store.electronicsstore.service.exceptions.ProductAlreadyExistsException;
import com.store.electronicsstore.service.exceptions.ProductNotFoundException;
import com.store.electronicsstore.service.exceptions.ProductsNotPresentException;

@Service
public class ProductService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
	private final ProductRepository productRepository;

	private final ModelMapper modelMapper;

	@Autowired
	public ProductService(ProductRepository productRepository, ModelMapper modelMapper) {
		this.productRepository = productRepository;
		this.modelMapper = modelMapper;
	}

	/**
	 * Method to add a new product to the store.
	 * 
	 * @param productDto
	 */
	@Transactional
	public void addNewProduct(ProductDto productDto) {
		Optional<Product> productById = productRepository.findByProductId(productDto.getProductId());
		productById.ifPresent(product -> {
			throw new ProductAlreadyExistsException(
					"Product with model number " + productDto.getProductId() + " already exists");
		});
		if (!productById.isPresent()) {
			LOGGER.info("No Duplicates found.");
			Product product = modelMapper.map(productDto, Product.class);

			// Save Product
			productRepository.save(product);
		}
	}

	/**
	 * Method to update inventory for a given product.
	 *
	 * @param pid
	 * @param quantityToAdd
	 */

	public void updateQuantity(String pid, int updatedInventory) {

		LOGGER.info("Retrieving existing product:" + pid);

		Product product = productRepository.findByProductId(pid)
				.orElseThrow(() -> new ProductNotFoundException("Product with id:" + pid + " not found."));

		LOGGER.info("Updating inventory to: " + updatedInventory);
		product.setInventory(updatedInventory);
		productRepository.save(product);
	}

	/**
	 * Method to retrieve a product by id.
	 * 
	 * @param pid
	 * @return
	 */
	public ProductDto getProductById(String pid) {
		LOGGER.info("Retreiving product for : " + pid);
		Product product = productRepository.findByProductId(pid)
				.orElseThrow(() -> new ProductNotFoundException("Product with id:" + pid + " is not found."));

		return modelMapper.map(product, ProductDto.class);
	}

	/**
	 * Method to retrieve all products.
	 * 
	 * @return
	 */
	public List<ProductDto> getAllProducts() {
		List<Product> products = (List<Product>) productRepository.findAll();

		if (products.isEmpty()) {
			LOGGER.info("No Products exist");
			throw new ProductsNotPresentException("no products present in DB");
		}

		return mapProductListToProDtoList(products);
	}

	/**
	 * Method to return available inventory of a product.
	 * 
	 * @param pid
	 * @return
	 */
	public int getNumberOfProductsById(String pid) {
		Optional<Product> product = productRepository.findByProductId(pid);

		return product.isPresent() ? product.get().getInventory() : 0;
	}

	@Transactional
	public void updateProduct(String pid, ProductDto productDto) {

		Product product = productRepository.findByProductId(pid)
				.orElseThrow(() -> new ProductNotFoundException("Product with id: " + pid + " is not found."));
		LOGGER.info("Product to be updated : " + product.getName());
		if (productDto.getProductId() != null) {
			if (!productDto.getProductId().equals(pid)) {
				throw new ProductNotFoundException("Id cannot be updated.");
			}
		}
		product.setInventory(productDto.getInventory());
		product.setName(productDto.getName());
		product.setManufacturer(productDto.getManufacturer());
		product.setPrice(productDto.getPrice());
		productRepository.save(product);

	}

	/**
	 * Utility method to map product.
	 * 
	 * @param products
	 * @return list of products.
	 */
	private List<ProductDto> mapProductListToProDtoList(List<Product> products) {
		return products.stream().map(product -> modelMapper.map(product, ProductDto.class))
				.collect(Collectors.toList());
	}

	public void removeProduct(String pid) {
		try {
			productRepository.deleteByProductId(pid);
		} catch (Exception e) {
			LOGGER.error("Product not found with exception: " + e.getMessage());
		}

	}

}
