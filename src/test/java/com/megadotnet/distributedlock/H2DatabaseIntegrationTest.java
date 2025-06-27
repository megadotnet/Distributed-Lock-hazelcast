package com.megadotnet.distributedlock;

import com.megadotnet.distributedlock.entity.Product;
import com.megadotnet.distributedlock.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("h2")
public class H2DatabaseIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void contextLoads() {
        // 测试应用程序上下文是否成功加载
        assertNotNull(productRepository);
    }

    @Test
    public void testDatabaseOperations() {
        // 创建并保存产品
        Product product = new Product();
        product.setSeq(100);
        Product savedProduct = productRepository.save(product);

        // 验证产品已保存
        assertNotNull(savedProduct.getId());

        // 验证可以从数据库中检索产品
        assertTrue(productRepository.findById(savedProduct.getId()).isPresent());
    }
}
