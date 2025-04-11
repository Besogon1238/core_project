package com.example.coreservice.service;

import com.example.coreservice.model.Product;
import com.example.coreservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepo;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product(1L, "Laptop", "High-performance laptop", 999.99, 10);
        product2 = new Product(2L, "Smartphone", "Flagship model", 699.99, 15);
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        when(productRepo.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.getAllProducts();

        assertEquals(2, products.size());
        assertTrue(products.contains(product1));
        assertTrue(products.contains(product2));
        verify(productRepo, times(1)).findAll();
    }

    @Test
    void getProductById_ShouldReturnProduct() {
        when(productRepo.findById(1L)).thenReturn(Optional.of(product1));

        Product foundProduct = productService.getProductById(1L);

        assertNotNull(foundProduct);
        assertEquals(product1.getId(), foundProduct.getId());
        assertEquals(product1.getName(), foundProduct.getName());
        assertEquals(product1.getPrice(), foundProduct.getPrice());
        verify(productRepo, times(1)).findById(1L);
    }

    @Test
    void getProductById_WhenNotFound_ShouldThrowException() {
        when(productRepo.findById(99L)).thenReturn(Optional.empty());

        Product foundProduct = productService.getProductById(99L);

        assertNull(foundProduct);
        verify(productRepo, times(1)).findById(99L);
    }

    @Test
    void createProduct_ShouldSaveAndReturnProduct() {
        when(productRepo.save(product1)).thenReturn(product1);

        Product savedProduct = productService.createProduct(product1);

        assertNotNull(savedProduct);
        assertEquals(product1.getId(), savedProduct.getId());
        assertEquals(product1.getName(), savedProduct.getName());
        verify(productRepo, times(1)).save(product1);
    }

    @Test
    void updateProduct_ShouldUpdateExistingProduct() {
        Product updatedProduct = new Product(1L, "Laptop Pro", "Updated model", 1099.99, 5);
        when(productRepo.save(updatedProduct)).thenReturn(updatedProduct);

        Product result = productService.updateProduct(1L, updatedProduct);

        assertNotNull(result);
        assertEquals(updatedProduct.getName(), result.getName());
        assertEquals(updatedProduct.getPrice(), result.getPrice());
        verify(productRepo, times(1)).save(updatedProduct);
    }

    @Test
    void updateProduct_WhenNotFound_ShouldThrowException() {
        Product updatedProduct = new Product(99L, "Non-existent", "No product", 0.0, 0);

        when(productRepo.save(updatedProduct)).thenReturn(null);

        productService.updateProduct(99L, updatedProduct);

        verify(productRepo, times(1)).save(updatedProduct);

    }

    @Test
    void deleteProduct_ShouldCallRepository() {
        doNothing().when(productRepo).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepo, times(1)).deleteById(1L);
    }
}