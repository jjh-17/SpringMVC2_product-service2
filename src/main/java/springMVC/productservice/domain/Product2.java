package springMVC.productservice.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

import java.util.List;

@Data
public class Product2 {

    //Bean Validation
    @NotNull(groups = UpdateCheck.class) //NotNull은 상품 수정 시에만 적용
    private Long id;

    @NotBlank(message = "공백X", groups = {SaveCheck.class, UpdateCheck.class})
    private String name;

    @NotNull(message = "공백이여서는 안됩니다.", groups = {SaveCheck.class, UpdateCheck.class})
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull(message = "공백이여서는 안됩니다.", groups = {SaveCheck.class, UpdateCheck.class})
    @Max(value = 9999, groups = SaveCheck.class) //상품 추가 시에는 최대값 적용
    private Integer quantity;

    public Product2() {

    }

    public Product2(String name, Integer price, Integer quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
