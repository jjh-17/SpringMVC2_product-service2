package springMVC.productservice;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import springMVC.productservice.domain.Product;
import springMVC.productservice.domain.Product2;
import springMVC.productservice.domain.ProductRepository;
import springMVC.productservice.domain.ProductRepository2;

@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final ProductRepository productRepository;
    private final ProductRepository2 productRepository2;

    //테스트용 데이터 추가
    @PostConstruct
    public void testInit() {
        productRepository.save(new Product("A", 10000, 10));
        productRepository.save(new Product("B", 20000, 20));

        productRepository2.save(new Product2("A", 10000, 10));
        productRepository2.save(new Product2("B", 20000, 20));
    }
}
