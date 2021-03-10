package com.store.electronicsstore.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representation of Product Table.
 **/
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products", uniqueConstraints = @UniqueConstraint(columnNames= {"productId"}))
public class Product {
	/**
	 * Unique internal id.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRODUCT")
	@SequenceGenerator(name = "SEQ_PRODUCT", allocationSize = 1)
	private Long id;


	/**
	 * A unique product model number.
	 */
	private String productId;

	/**
	 * name of the product
	 */

	@NotNull
	private String name;

	/**
	 * Manufacturer of the Product
	 */
	private String manufacturer;

	/**
	 * price of the Product
	 */
	@Min(value = 0, message = "Price should be positive value.")
	private float price;

	/**
	 * Available Inventory of a product.
	 */
	@Min(value = 0, message = "Inventory should be positive value.")
	private int inventory;

}
