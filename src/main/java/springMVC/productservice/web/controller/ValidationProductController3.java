package springMVC.productservice.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springMVC.productservice.domain.Product2;
import springMVC.productservice.domain.ProductRepository2;
import springMVC.productservice.domain.SaveCheck;
import springMVC.productservice.domain.UpdateCheck;

import java.util.List;

/*
[Bean Validation]
검증 기준을 해당 객체 내에서 처리
Bean Validation 사용 시 스프링 부트가 자동으로 글로벌 Validator로 등록
사용자 설정 글로벌 Validator 등록 시, Bean Validator는 글로벌로 등록되지 않는다

[검증 순서]
1.@ModelAttribute 각각의 필드에 타입 변환 시도
    -실패 시 typeMismatch로 FieldError 추가
2. Validator 적용 ==> Binding이 성공한 Field에 대해서만 Validator 적용

EX)
    -name에 A 입력 ==> 타입 변환 성공 ==> name 필드에 Bean Validation 적용
    -price에 A 입력 ==> 타입 변환 실패 ==> typeMismatch Field Error 추가

[메시지 적용 순서]
1. 생성된 메시지 코드 순으로 messageSource에서 탐색
2. 애노테이션의 message 속성 사용 ==> @NotBlank(message = "~~")
3. 라이브러리가 제공하는 기본 값 사용
 */
@Slf4j
@Controller
@RequestMapping("/validation/v3/products")
@RequiredArgsConstructor
public class ValidationProductController3 {

    private final ProductRepository2 productRepository;

    //모든 상품 리스트
    @GetMapping
    public String products(Model model) {
        List<Product2> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "validation/v3/products";
    }

    //상품 상세 정보
    @GetMapping("/{id}")
    public String product(@PathVariable long id, Model model) {
        Product2 product = productRepository.findById(id);
        model.addAttribute("product", product);
        return "validation/v3/product";
    }

    //상품 등록 - 입력 폼 처리
    @GetMapping("/add")
    public String addForm(Model model) {
        //입력할 상품 등록 폼 전달
        model.addAttribute("product", new Product2());
        return "validation/v3/addForm";
    }

    //WebDataBinder를 이용한 사용자 설정 validator 사용
    //@Validated: 대상에 대한 검증기를 실행하라 ==> 검증 클래스의 supports() 메서드를 이용하여 적합한 검증기를 찾아서 실행
    //@PostMapping("/add")
    public String addProduct(@Validated @ModelAttribute("product") Product2 product, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        //전체 예외
        if (product.getPrice() != null && product.getQuantity() != null) {
            int resultPrice = product.getPrice() * product.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000,
                        resultPrice}, null);
            }
        }

        //검증 실패 시 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v3/addForm";
        }

        Product2 savedProduct = productRepository.save(product);
        redirectAttributes.addAttribute("id", savedProduct.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/products/{id}";
    }

    //group을 적용한 Bean Validation ==> SaveCheck
    @PostMapping("/add")
    public String addProduct2(@Validated(SaveCheck.class) @ModelAttribute("product") Product2 product,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        //전체 예외
        if (product.getPrice() != null && product.getQuantity() != null) {
            int resultPrice = product.getPrice() * product.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000,
                        resultPrice}, null);
            }
        }

        //검증 실패 시 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v3/addForm";
        }

        Product2 savedProduct = productRepository.save(product);
        redirectAttributes.addAttribute("id", savedProduct.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/products/{id}";
    }

    //상품 수정 폼
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Product2 product = productRepository.findById(id);
        model.addAttribute("product", product);
        return "validation/v3/editForm";
    }

    //상품 수정 - @PathVariable : @Mapping된 URI 내 'id'에 할당된 값을 갖는다
    //@PostMapping("/{id}/edit")
    public String editProduct(@PathVariable Long id, @Validated @ModelAttribute("product") Product2 product,
                              BindingResult bindingResult) {

        //전체 예외
        if (product.getPrice() != null && product.getQuantity() != null) {
            int resultPrice = product.getPrice() * product.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000,
                        resultPrice}, null);
            }
        }

        //검증 실패 시 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v3/editForm";
        }

        productRepository.update(id, product);
        return "redirect:/validation/v3/products/{id}";
    }

    @PostMapping("/{id}/edit")
    public String editProduct2(@PathVariable Long id,
                               @Validated(UpdateCheck.class) @ModelAttribute("product") Product2 product,
                              BindingResult bindingResult) {

        //전체 예외
        if (product.getPrice() != null && product.getQuantity() != null) {
            int resultPrice = product.getPrice() * product.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000,
                        resultPrice}, null);
            }
        }

        //검증 실패 시 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v3/editForm";
        }

        productRepository.update(id, product);
        return "redirect:/validation/v3/products/{id}";
    }

}
