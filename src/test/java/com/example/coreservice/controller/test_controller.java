package com.example.coreservice.controller;

import com.example.coreservice.model.Product;
import com.example.coreservice.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        product1 = new Product(1L, "Laptop", "High-performance laptop", 999.99, 10);
        product2 = new Product(2L, "Smartphone", "Flagship smartphone", 699.99, 15);
    }

    @Test
    void getProductById_ShouldReturnProduct() throws Exception {
        when(productService.getProductById(1L)).thenReturn(product1);

        mockMvc.perform(get("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(999.99));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[0].price").value(999.99))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Smartphone"));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void getProductById_WhenNotExists_ShouldReturnNotFound() throws Exception {
        when(productService.getProductById(99L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        mockMvc.perform(get("/api/products/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getProductById(99L);
    }

    @Test
    void deleteProduct_ShouldReturnNoContent() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        when(productService.createProduct(any(Product.class))).thenReturn(product1);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(999.99));

        verify(productService, times(1)).createProduct(any(Product.class));
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct() throws Exception {
        Product updatedProduct = new Product(1L, "Laptop Pro", "Updated description", 1099.99, 5);
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop Pro"))
                .andExpect(jsonPath("$.price").value(1099.99));

        verify(productService, times(1)).updateProduct(eq(1L), any(Product.class));
    }
}