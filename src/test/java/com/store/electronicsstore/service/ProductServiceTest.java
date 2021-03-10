package com.store.electronicsstore.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import com.store.electronicsstore.domain.Product;
import com.store.electronicsstore.domain.ProductRepository;
import com.store.electronicsstore.service.dto.ProductDto;
import com.store.electronicsstore.service.exceptions.ProductAlreadyExistsException;
import com.store.electronicsstore.service.exceptions.ProductNotFoundException;
import com.store.electronicsstore.service.exceptions.ProductsNotPresentException;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {
    private final String productId = "A1013";
    private final int inventoryAvailable = 2;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    public void testAddNewProduct() {
        //given
        ProductDto productDto = mock(ProductDto.class);
        Product product = mock(Product.class);
        given(productDto.getProductId()).willReturn(productId);
        given(productRepository.findByProductId(productId)).willReturn(Optional.empty());
        given(modelMapper.map(productDto, Product.class)).willReturn(product);

        //when
        productService.addNewProduct(productDto);

        //then
        verify(productRepository).save(product);
    }

    @Test(expected = ProductAlreadyExistsException.class)
    public void testAddNewProduct_With_ExistingProduct_ThrowsProductAlreadyExistsException() {
        //given
        ProductDto productDto = mock(ProductDto.class);
        Product product = mock(Product.class);
        given(productDto.getProductId()).willReturn(productId);
        given(productRepository.findByProductId(productId)).willReturn(Optional.ofNullable(product));

        //when
        productService.addNewProduct(productDto);
    }

    @Test
    public void testGetProductById() {
        //given
        Product product = mock(Product.class);
        ProductDto productDto = mock(ProductDto.class);
        given(productRepository.findByProductId(productId)).willReturn(Optional.ofNullable(product));
        given(modelMapper.map(product, ProductDto.class)).willReturn(productDto);

        //when
        ProductDto actualProductDto = productService.getProductById(productId);

        //then
        assertEquals(productDto, actualProductDto);
    }

	@Test(expected = ProductNotFoundException.class)
    public void testGetProductById_When_productIdDoesNotExist_Then_ThrowsProductNotFoundException() {
        //given
        given(productRepository.findByProductId(productId)).willReturn(Optional.empty());

        //when
        productService.getProductById(productId);
    }

    @Test
    public void testGetAllProducts() {
        //given
        Product product = mock(Product.class);
        List<Product> productList = new ArrayList<>();
        productList.add(product);

        ProductDto productDto = mock(ProductDto.class);
        List<ProductDto> productDtoList = new ArrayList<>();
        productDtoList.add(productDto);
        given(productRepository.findAll()).willReturn(productList);
        given(modelMapper.map(product, ProductDto.class)).willReturn(productDto);

        //when
        List<ProductDto> actualProductDto = productService.getAllProducts();

        //then
        assertEquals(productDtoList, actualProductDto);
    }
    
    @Test
    public void testUpdateQuantity() {
        //given
    	 Product product = mock(Product.class);
         given(productRepository.findByProductId(productId)).willReturn(Optional.ofNullable(product));

        //when
        productService.updateQuantity(productId, 2);

        //then
        verify(productRepository).save(product);

    }
    
    @Test(expected = ProductsNotPresentException.class)
    public void testGetAllProducts_WhenNoProductExist_ThenThrowException() {
        //given
        given(productRepository.findAll()).willReturn(new ArrayList<>());

        //when
        productService.getAllProducts();

    }

    @Test
    public void testGetNumberOfProductsById() {
        //given
        Product product = mock(Product.class);
        given(productRepository.findByProductId(productId)).willReturn(Optional.ofNullable(product));
        given(product.getInventory()).willReturn(inventoryAvailable);

        //when
        int productInventory = productService.getNumberOfProductsById(productId);

        //then
        assertEquals(inventoryAvailable, productInventory);
    }

    @Test
    public void testGetNumberOfProductsById_Given_NoProductsIsPresent() {
        //given
        given(productRepository.findByProductId(productId)).willReturn(Optional.empty());

        //when
        int productInventory = productService.getNumberOfProductsById(productId);

        //then
        assertEquals(0, productInventory);
    }

   @Test
    public void testUpdateProduct() {
        //given
        ProductDto productDto = mock(ProductDto.class);
        Product product = mock(Product.class);
        given(productDto.getProductId()).willReturn(productId);
        given(productRepository.findByProductId(productId)).willReturn(Optional.ofNullable(product));

        //when
        productService.updateProduct(productId, productDto);

        //then
        verify(productRepository).save(product);
    }

   @Test(expected = ProductNotFoundException.class)
    public void testUpdateProduct_Given_IdDoesNotExist_Then_ThrowsProductNotFoundException() {
	 //given
       ProductDto productDto = mock(ProductDto.class);

       //when
       productService.updateProduct(productId, productDto);

    }
   
   @Test(expected = ProductNotFoundException.class)
   public void testUpdateProduct_Given_IdDoesNotMatch_Then_ThrowsProductNotFoundException() {
	 //given
	   ProductDto productDto = mock(ProductDto.class);
	   Product product = mock(Product.class);
	   given(productRepository.findByProductId(productId)).willReturn(Optional.ofNullable(product));
       given(productDto.getProductId()).willReturn("");

      //when
      productService.updateProduct(productId, productDto);

   }

   @Test
   public void testRemoveProduct() {

       //when
       productService.removeProduct(productId);

       //then
       verify(productRepository, times(1)). deleteByProductId(productId);
   }

}
