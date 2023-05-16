package springMVC.productservice.web.controller.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springMVC.productservice.web.validator.ProductSaveForm;

/*
[BeanValidation - HTTP 메시지 컨버터]
1. 성공 요청: 성공
2. 실패 요청: JSON을 객체로 생성하는 것 자체 실패
3. 검증 오류 요청: JSON 객체 생성 성공, 검증 실패

@ModelAttribute: 필드 단위로 정교하게 바인딩 적용. 특정 필드의 타입이 틀려도 나머지 필드에 대한 검증은 정상 처리
@RequestBody: 전체 객체 단위로 적용 ==> 메시지 컨버터 작동 성공으로 객체가 생성되어야 검증 시작

 */
@Slf4j
@RestController
@RequestMapping("/validation/api/products")
public class ValidationProductApiController {

    //타입 오류 발생 시 JSON 객체 생성에 실패 ==> 컨트롤러 호출 X
    @PostMapping("/add")
    public Object addProduct(@RequestBody @Validated ProductSaveForm form, BindingResult bindingResult) {
        log.info("API 컨트롤러 호출");

        if (bindingResult.hasErrors()) {
            log.info("검증 오류 발생 {}", bindingResult);
            return bindingResult.getAllErrors();
        }

        log.info("성공 로직 실행");
        return form;
    }
}
