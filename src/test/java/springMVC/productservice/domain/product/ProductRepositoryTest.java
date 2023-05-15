package springMVC.productservice.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ProductRepositoryTest {

    ProductRepository productRepository = new ProductRepository();

    @AfterEach
    void afterEach() {
        productRepository.clearStore();
    }

    @Test
    public void save() throws Exception {
        //given
        Product productA = new Product("productA", 10000, 10);

        //when
        Product saved = productRepository.save(productA);

        //then
        Product found = productRepository.findById(saved.getId());
        Assertions.assertThat(found).isEqualTo(saved);
    }

    @Test
    public void findAll() throws Exception {
        //given
        Product productA = new Product("productA", 10000, 10);
        Product productB = new Product("productB", 20000, 20);

        productRepository.save(productA);
        productRepository.save(productB);

        //when
        List<Product> list = productRepository.findAll();

        //then
        Assertions.assertThat(list.size()).isEqualTo(2);
        Assertions.assertThat(list).contains(productA, productB);
    }

    @Test
    public void update() throws Exception {
        //given
        Product productA = new Product("productA", 10000, 10);
        Product saved = productRepository.save(productA);
        Long id = saved.getId();


        //when
        Product productB = new Product("productB", 20000, 20);

        productRepository.update(id, productB);
        Product found = productRepository.findById(id);

        //then
        Assertions.assertThat(found.getName()).isEqualTo(productB.getName());
        Assertions.assertThat(found.getPrice()).isEqualTo(productB.getPrice());
        Assertions.assertThat(found.getQuantity()).isEqualTo(productB.getQuantity());
    }

}
