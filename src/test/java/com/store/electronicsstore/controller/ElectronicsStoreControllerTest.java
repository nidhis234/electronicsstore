package com.store.electronicsstore.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.electronicsstore.service.ProductService;
import com.store.electronicsstore.service.dto.ProductDto;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElectronicsStoreControllerTest {
    private final Long id = 2134L;
    private final String name = "name";
    private final String manufacturer = "manufacturer";
    private final float price = 25;
    private final int inventory = 2;
    private final String productId = "product12";
    private final int updateInventory = 1;

    private MockMvc mockMvc;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ElectronicsStoreController electronicsStoreController;

    @MockBean
    private ProductService productService;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(this.electronicsStoreController).build();
    }

    @Test
    public void testAddNewProduct() throws Exception {
        //given
        ProductDto productDto = ProductDto.builder()
                .id(id).inventory(inventory).manufacturer(manufacturer).name(name).price(price).productId(productId).build();

        doNothing().when(productService).addNewProduct(productDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/addNewProduct")
                .content(objectMapper.writeValueAsBytes(productDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }


    @Test
    public void testGetProductById() throws Exception {
        // given
        String url = "/v1/product/" + productId;
        ProductDto productDto = createProductDto();
        given(productService.getProductById(productId)).willReturn(productDto);

        // then
        mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllProducts() throws Exception {
        //given
        List<ProductDto> productDtoList = Arrays.asList(createProductDto());
        given(productService.getAllProducts()).willReturn(productDtoList);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/products")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].inventory").isNotEmpty());
    }

    @Test
    public void testGetNumberOfProductsById() throws Exception {
        //given
        String url = "/v1/inventory/" + productId;
        given(productService.getNumberOfProductsById(productId)).willReturn(inventory);
        // then
        mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .accept(MediaType.ALL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(inventory)));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        //given
        String url = "/v1/updateProduct/" + productId;
        ProductDto productDto = createProductDto();
        doNothing().when(productService).updateProduct(productId, productDto);

        //then
        mockMvc.perform(MockMvcRequestBuilders
                .put(url)
                .content(objectMapper.writeValueAsBytes(productDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testUpdateInventoryOfProduct() throws Exception {
        //given
        String url = "/v1/updateInventory/" + productId + "/" + updateInventory;
        ProductDto productDto = createProductDto();
        doNothing().when(productService).updateProduct(productId, productDto);

        //then
        mockMvc.perform(MockMvcRequestBuilders
                .put(url)
                .content(objectMapper.writeValueAsBytes(productDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveProduct() throws Exception {
        //given
        String url = "/v1/product/" + productId;
        doNothing().when(productService).removeProduct((productId));

        //then
        mockMvc.perform(MockMvcRequestBuilders
                .delete(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }
   
    private ProductDto createProductDto() {
        return ProductDto.builder()
                .id(id).inventory(inventory).manufacturer(manufacturer).name(name).price(price).productId(productId).build();
    }


}
