package com.store.electronicsstore.service.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

	/**
	 * Id.
	 */
	@JsonIgnore
	@ApiModelProperty(hidden = true)
	private Long id;

	/**
	 * Product Id of a product.
	 */
	@NotNull
	private String productId;

	/**
	 * name of the product
	 */
	@NotNull
	private String name;

	@ApiModelProperty(value = "manufacturer of the Product")
	private String manufacturer;

	/**
	 * price of the Product
	 */
	@ApiModelProperty(value = "Price of the Product")
	@Min(value = 0, message = "Price should be positive value.")
	private float price;

	/**
	 * Amount of Product available
	 */
	@ApiModelProperty(value = "Product available on the store")
	@Min(value = 0, message = "Inventory should be positive value.")
	private int inventory;

}
