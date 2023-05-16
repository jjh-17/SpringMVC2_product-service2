package springMVC.productservice.domain.product;

import lombok.Data;
import java.util.List;

@Data
public class Product {

    //기본 정보
    private Long id;
    private String name;
    private Integer price; //null이 할당될 수 있으므로 int 대신 Integer
    private Integer quantity;

    //스프링 통합, 폼
    private Boolean open; //판매 여부
    private List<String> regions; //등록 지역
    private ProductType productType; //상품 종류
    private String deliveryCode; //배송 방식


    public Product() {

    }

    public Product(String name, Integer price, Integer quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
