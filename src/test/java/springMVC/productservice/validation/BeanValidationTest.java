package springMVC.productservice.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import springMVC.productservice.domain.Product2;

import java.util.Set;

public class BeanValidationTest {

    //Bean Validation을 적용한 테스트
    @Test
    public void beanValidation() throws Exception {
        //given
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        //when ==> 잘못된 상품 정보
        Product2 product = new Product2();
        product.setName(" ");
        product.setPrice(0);
        product.setQuantity(10000);

        //then
        Set<ConstraintViolation<Product2>> violations = validator.validate(product);
        for (ConstraintViolation<Product2> violation : violations) {
            System.out.println("violation = " + violation);
            System.out.println("violation.getMessage() = " + violation.getMessage());
        }
    }
}
