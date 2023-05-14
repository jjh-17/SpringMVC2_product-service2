package springMVC.productservice.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.FieldError;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.ObjectError;

import java.util.Arrays;

public class MessageCodesResolverTest {

    MessageCodesResolver messageCodesResolver = new DefaultMessageCodesResolver();

    /*
    [객체 오류 코드 생성 법칙]
    1. code + object name
    2. code
     */
    @Test
    public void messageCodeResolverObject() throws Exception {
        //given : 메시지 코드 생성
        String[] messageCodes = messageCodesResolver.resolveMessageCodes(
                "required", "product");

        //when
        Assertions.assertThat(messageCodes).containsExactly("required.product", "required");

        //then
        System.out.println("messageCodes = " + Arrays.toString(messageCodes));

//        new ObjectError("product", messageCodes, null);
    }

    /*
    [필드 오류 코드 생성 법칙]
    1. code + object name + field
    2. code + field
    3. code + field type
    4. code
     */
    @Test
    public void messageCodeResolverField() throws Exception {
        //given
        String[] messageCodes = messageCodesResolver.resolveMessageCodes(
                "required", "product", "name", String.class);

        //when ==> 이 순서로 저장되어 있다
        Assertions.assertThat(messageCodes).containsExactly(
                "required.product.name", "required.name",
                "required.java.lang.String", "required");

        //then
        System.out.println("messageCodes = " + Arrays.toString(messageCodes));

//        new FieldError("product", "name", null, false, messageCodes, null, null);
    }

}
