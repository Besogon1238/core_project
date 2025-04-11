package com.example.coreservice.repository;

import com.example.coreservice.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class ProductRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldSaveProduct() {
        Product product = new Product();
        product.setName("Laptop");
        product.setDescription("High-performance laptop");
        product.setPrice(999.99);
        product.setQuantity(10);

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("Laptop");
        assertThat(savedProduct.getPrice()).isEqualTo(999.99);
        assertThat(savedProduct.getQuantity()).isEqualTo(10);
    }

    @Test
    void shouldFindProductById() {

        Product product = new Product();
        product.setName("Smartphone");
        product.setDescription("Flagship model");
        product.setPrice(699.99);
        product.setQuantity(5);
        Product savedProduct = productRepository.save(product);

        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isEqualTo("Smartphone");
        assertThat(foundProduct.get().getPrice()).isEqualTo(699.99);
    }

    @Test
    void shouldUpdateProduct() {
        Product product = new Product();
        product.setName("Tablet");
        product.setDescription("10-inch display");
        product.setPrice(299.99);
        product.setQuantity(3);
        Product savedProduct = productRepository.save(product);

        savedProduct.setPrice(249.99);
        savedProduct.setQuantity(5);
        Product updatedProduct = productRepository.save(savedProduct);

        Optional<Product> foundProduct = productRepository.findById(updatedProduct.getId());
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getPrice()).isEqualTo(249.99);
        assertThat(foundProduct.get().getQuantity()).isEqualTo(5);
    }

    @Test
    void shouldDeleteProduct() {

        Product product = new Product();
        product.setName("Monitor");
        product.setDescription("27-inch 4K");
        product.setPrice(399.99);
        product.setQuantity(2);
        Product savedProduct = productRepository.save(product);

        productRepository.deleteById(savedProduct.getId());

        assertThat(productRepository.findById(savedProduct.getId())).isEmpty();
    }

    @Test
    void shouldNotFindNonExistingProduct() {
        assertThat(productRepository.findById(999L)).isEmpty();
    }
}