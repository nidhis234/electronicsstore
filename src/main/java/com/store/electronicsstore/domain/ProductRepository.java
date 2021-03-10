package com.store.electronicsstore.domain;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

	Optional<Product> findByProductId(String productId);
	
	Product deleteByProductId(String productId);
}
