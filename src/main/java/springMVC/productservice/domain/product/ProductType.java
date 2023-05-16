package springMVC.productservice.domain.product;

import lombok.Getter;

//상품 종류
@Getter
public enum ProductType {
    BOOK("도서"), FOOD("음식"), ETC("기타");

    private final String description;

    ProductType(String description) {
        this.description = description;
    }
}
