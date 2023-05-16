package springMVC.productservice.domain.repository;

import org.springframework.stereotype.Repository;
import springMVC.productservice.domain.product.Product2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository //ComponentScan의 대상이 된다
public class ProductRepository2 {

    //static: 메모리 static 영역에 적재 ==> 공통적으로 사용 가능
    private static final Map<Long, Product2> store = new HashMap<>();
    private static long sequence = 0L;


    //상품 저장
    public Product2 save(Product2 product) {
        product.setId(++sequence);
        store.put(product.getId(), product);

        return product;
    }

    //상품 조회 - Id
    public Product2 findById(Long id) {
        return store.get(id);
    }

    //상품 조회 - 전체
    public List<Product2> findAll() {
        return new ArrayList<>(store.values());
    }

    //상품 수정
    public void update(Long id, Product2 newProduct) {
        Product2 product = findById(id);

        product.setName(newProduct.getName());
        product.setPrice(newProduct.getPrice());
        product.setQuantity(newProduct.getQuantity());
    }

    //상품 전체 삭제
    public void clearStore() {
        store.clear();
    }

}
