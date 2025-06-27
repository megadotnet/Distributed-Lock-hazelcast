package com.megadotnet.distributedlock.repository;

import com.megadotnet.distributedlock.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("h2")
public class ProductRepositoryH2Test {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testSaveAndFindProduct() {
        // 创建并保存产品
        Product product = new Product();
        product.setSeq(1);
        Product savedProduct = productRepository.save(product);

        // 验证产品已保存并分配了ID
        assertNotNull(savedProduct.getId());
        assertEquals(1, savedProduct.getSeq());

        // 通过ID查找产品
        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());
        assertTrue(foundProduct.isPresent());
        assertEquals(1, foundProduct.get().getSeq());
    }

    @Test
    public void testFindMaxSeq() {
        // 创建并保存多个产品
        Product product1 = new Product();
        product1.setSeq(5);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setSeq(10);
        productRepository.save(product2);

        // 测试findMaxSeq方法
        Optional<Integer> maxSeq = productRepository.findMaxSeq();
        assertTrue(maxSeq.isPresent());
        assertEquals(10, maxSeq.get());
    }
}
