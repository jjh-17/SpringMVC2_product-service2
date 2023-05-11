package springMVC.productservice;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import springMVC.productservice.domain.Product;
import springMVC.productservice.domain.ProductRepository;

@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final ProductRepository productRepository;

    //테스트용 데이터 추가
    @PostConstruct
    public void testInit() {
        productRepository.save(new Product("A", 10000, 10));
        productRepository.save(new Product("B", 20000, 20));
    }
}
