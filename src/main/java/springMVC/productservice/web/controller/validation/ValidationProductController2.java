package springMVC.productservice.web.controller.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springMVC.productservice.domain.product.Product;
import springMVC.productservice.domain.repository.ProductRepository;
import springMVC.productservice.web.validator.ProductValidator;

import java.util.List;

/*
검증 요구사항 추가
1. 타입 검증
    -가격, 수량 문자 포함 여부 검증
2. 필드 검증
    -상품명 필수, 공백 X
    -1000 <= 가격 <= 1000,000
    -수량 최대 9999
3. 특정 필드 범위 검증
    -가격*수량은 10000이상
 */
@Slf4j
@Controller
@RequestMapping("/validation/v2/products")
@RequiredArgsConstructor
public class ValidationProductController2 {

    private final ProductRepository productRepository;
    private final ProductValidator productValidator; //Product 검증

    //사용자 설정 validator를 WebDataBinder에 등록
    @InitBinder
    public void init(WebDataBinder dataBinder) {
        log.info("init binder {}", dataBinder);
        dataBinder.addValidators(productValidator);
    }

    //모든 상품 리스트
    @GetMapping
    public String products(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "validation/v2/products";
    }

    //상품 상세 정보
    @GetMapping("/{id}")
    public String product(@PathVariable long id, Model model) {
        Product product = productRepository.findById(id);
        model.addAttribute("product", product);
        return "validation/v2/product";
    }

    //상품 등록 - 입력 폼 처리
    @GetMapping("/add")
    public String addForm(Model model) {
        //입력할 상품 등록 폼 전달
        model.addAttribute("product", new Product());
        return "validation/v2/addForm";
    }

    //BindingResult : @ModelAttribute Product product 바로 다음에 와야 함
    //                      ==> product의 바인딩 결과를 담기 위해서
//    @PostMapping("/add")
    public String addProductV1(@ModelAttribute Product product, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        //검증 로직 - 단일 필드
        if (!StringUtils.hasText(product.getName())) {
            bindingResult.addError(new FieldError(
                    "product", "name", "상품 이름은 필수입니다."));
        }
        if (product.getPrice() == null || product.getPrice() < 1000 || product.getPrice() > 1000000) {
            bindingResult.addError(new FieldError(
                    "product", "price", "가격은 1000원 이상 100만원 이하까지 허용됩니다."));
        }
        if(product.getQuantity() == null || product.getQuantity() < 0 || product.getQuantity() > 9999){
            bindingResult.addError(new FieldError(
                    "product", "quantity", "수량은 최대 9,999까지 허용됩니다."));
        }

        //검증 로직 - 복합 필드
        if(product.getPrice() != null && product.getQuantity() != null){
            int totalPrice = product.getPrice() * product.getQuantity();

            if (totalPrice < 10000) {
                bindingResult.addError(new ObjectError("product",
                        "총 금액은 10,000원 이상이어야 합니다. 현재 값 = " + totalPrice));
            }
        }

        //검증 실패 시 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }


        Product savedProduct = productRepository.save(product);
        redirectAttributes.addAttribute("id", savedProduct.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/products/{id}";
    }

    /*
    [FieldError 생성자]
    -objectName: 오류 발생 객체 이름
    -field: 오류 필드
    -rejectedValue: 사용자가 입력한 값(거절된 값)
    -bindingFailure: 타입 오류와 같은 바인딩 실패(true), 검증 실패(false)
    -codes: 메시지 코드
    -arguments: 메시지에서 사용하는 인자
    -defaultMessage: default 오류 메시지
     */
//    @PostMapping("/add")
    public String addProductV2(@ModelAttribute Product product, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        //검증 로직 - 단일 필드
        if (!StringUtils.hasText(product.getName())) {
            bindingResult.addError(new FieldError(
                    "product", "name", product.getName(), false, null, null,
                    "상품 이름은 필수입니다."));
        }
        if (product.getPrice() == null || product.getPrice() < 1000 || product.getPrice() > 1000000) {
            bindingResult.addError(new FieldError(
                    "product", "price", product.getPrice(), false, null, null,
                    "가격은 1000원 이상 100만원 이하까지 허용됩니다."));
        }
        if(product.getQuantity() == null || product.getQuantity() < 0 || product.getQuantity() > 9999){
            bindingResult.addError(new FieldError(
                    "product", "quantity", product.getQuantity(), false, null, null,
                    "수량은 최대 9,999까지 허용됩니다."));
        }

        //검증 로직 - 복합 필드
        if(product.getPrice() != null && product.getQuantity() != null){
            int totalPrice = product.getPrice() * product.getQuantity();

            if (totalPrice < 10000) {
                bindingResult.addError(new ObjectError("product", null, null,
                        "총 금액은 10,000원 이상이어야 합니다. 현재 값 = " + totalPrice));
            }
        }

        //검증 실패 시 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        Product savedProduct = productRepository.save(product);
        redirectAttributes.addAttribute("id", savedProduct.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/products/{id}";
    }

    //메시지 설정 추가
//    @PostMapping("/add")
    public String addProductV3(@ModelAttribute Product product, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        //검증 로직 - 단일 필드
        if (!StringUtils.hasText(product.getName())) {
            bindingResult.addError(new FieldError(
                    "product", "name", product.getName(), false,
                    new String[]{"required.product.name"}, null, null));
        }
        if (product.getPrice() == null || product.getPrice() < 1000 || product.getPrice() > 1000000) {
            bindingResult.addError(new FieldError(
                    "product", "price", product.getPrice(), false,
                    new String[]{"range.product.price"}, new Object[]{1000, 1000000}, null));
        }
        if(product.getQuantity() == null || product.getQuantity() < 0 || product.getQuantity() > 9999){
            bindingResult.addError(new FieldError(
                    "product", "quantity", product.getQuantity(), false,
                    new String[]{"max.product.quantity"}, new Object[]{9999}, null));
        }

        //검증 로직 - 복합 필드
        if(product.getPrice() != null && product.getQuantity() != null){
            int totalPrice = product.getPrice() * product.getQuantity();

            if (totalPrice < 10000) {
                bindingResult.addError(new ObjectError("product",
                        new String[]{"totalPriceMin"}, new Object[]{10000, totalPrice}, null));
            }
        }

        //검증 실패 시 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }


        Product savedProduct = productRepository.save(product);
        redirectAttributes.addAttribute("id", savedProduct.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/products/{id}";
    }

    /*
    rejectValue(), reject()로 FieldError, ObjectError 대체

    [rejectValue()]
    -field: 오류 필드명
    -errorCode: 오류 코드 ==>
    -errorArgs: 오류 메시지의 {} 치환을 위한 값
    -defaultMessage: 오류 메시지를 찾을 수 없을 때 기본 메시지
     */
//    @PostMapping("/add")
    public String addProductV4(@ModelAttribute Product product, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        //검증 로직 - 단일 필드
//        if (!StringUtils.hasText(product.getName())) {
//            bindingResult.rejectValue("name", "required");
//        }
        //Empty거나 공백일 때 대체 가능
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "name", "required");

        if (product.getPrice() == null || product.getPrice() < 1000 || product.getPrice() > 1000000) {
            bindingResult.rejectValue("price", "range",
                    new Object[]{1000, 1000000}, null);
        }
        if(product.getQuantity() == null || product.getQuantity() < 0 || product.getQuantity() > 9999){
            bindingResult.rejectValue("quantity", "max",
                    new Object[]{9999}, null);
        }

        //검증 로직 - 복합 필드
        if(product.getPrice() != null && product.getQuantity() != null){
            int totalPrice = product.getPrice() * product.getQuantity();

            if (totalPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, totalPrice}, null);
            }
        }

        //검증 실패 시 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }


        Product savedProduct = productRepository.save(product);
        redirectAttributes.addAttribute("id", savedProduct.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/products/{id}";
    }

    //검증을 다른 클래스로 분리
    @PostMapping("/add")
    public String addProductV5(@ModelAttribute Product product, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        //검증 단순화
        productValidator.validate(product, bindingResult);

        //검증 실패 시 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }


        Product savedProduct = productRepository.save(product);
        redirectAttributes.addAttribute("id", savedProduct.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/products/{id}";
    }

    //WebDataBinder를 이용한 사용자 설정 validator 사용
    //@Validated: 대상에 대한 검증기를 실행하라 ==> 검증 클래스의 supports() 메서드를 이용하여 적합한 검증기를 찾아서 실행
//    @PostMapping("/add")
    public String addProductV6(@Validated @ModelAttribute Product product, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        //검증 실패 시 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }


        Product savedProduct = productRepository.save(product);
        redirectAttributes.addAttribute("id", savedProduct.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/products/{id}";
    }

    //상품 수정 폼
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id);
        model.addAttribute("product", product);
        return "validation/v2/editForm";
    }

    //상품 수정 - @PathVariable : @Mapping된 URI 내 'id'에 할당된 값을 갖는다
    @PostMapping("/{id}/edit")
    public String editProduct(@PathVariable Long id, @Validated @ModelAttribute Product product,
                              BindingResult bindingResult) {
        //검증 실패 시 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v2/editForm";
        }

        productRepository.update(id, product);
        return "redirect:/validation/v2/products/{id}";
    }

}
