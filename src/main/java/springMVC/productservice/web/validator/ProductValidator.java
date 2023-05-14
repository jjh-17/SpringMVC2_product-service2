package springMVC.productservice.web.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import springMVC.productservice.domain.Product;

//검증 로직을 별도로 분리
@Component
@Slf4j
public class ProductValidator implements Validator {


    //validator 지원 여부 확인
    @Override
    public boolean supports(Class<?> clazz) {
        //clazz가 Product 타입이거나 Product의 자식 클래스인가
        return Product.class.isAssignableFrom(clazz);
    }

    //target: 검증 대상 객체, errors: BindingResult
    @Override
    public void validate(Object target, Errors errors) {
        Product product = (Product) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required");

        if (product.getPrice() == null || product.getPrice() < 1000 || product.getPrice() > 1000000) {
            errors.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }
        if(product.getQuantity() == null || product.getQuantity() < 0 || product.getQuantity() > 9999){
            errors.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        //검증 로직 - 복합 필드
        if(product.getPrice() != null && product.getQuantity() != null){
            int totalPrice = product.getPrice() * product.getQuantity();

            if (totalPrice < 10000) {
                errors.reject("totalPriceMin", new Object[]{10000, totalPrice}, null);
            }
        }
    }
}
