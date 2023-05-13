package springMVC.productservice.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springMVC.productservice.domain.Product;
import springMVC.productservice.domain.ProductRepository;

import java.util.HashMap;
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
@RequestMapping("/validation/v1/products")
@RequiredArgsConstructor
public class ValidationProductController1 {

    private final ProductRepository productRepository;

    //모든 상품 리스트
    @GetMapping
    public String products(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "validation/v1/products";
    }

    //상품 상세 정보
    @GetMapping("/{id}")
    public String product(@PathVariable long id, Model model) {
        Product product = productRepository.findById(id);
        model.addAttribute("product", product);
        return "validation/v1/product";
    }

    //상품 등록 - 입력 폼 처리
    @GetMapping("/add")
    public String addForm(Model model) {
        //입력할 상품 등록 폼 전달
        model.addAttribute("product", new Product());
        return "validation/v1/addForm";
    }

    //상품 추가 : RedirectAttributes : URL 인코딩 수행
    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product, RedirectAttributes redirectAttributes, Model model) {
        //검증 오류 결과 보관
        HashMap<String, String> errors = new HashMap<>();

        //검증 로직 - 단일 필드
        if (!StringUtils.hasText(product.getName())) {
            errors.put("name", "상품 이름은 필수입니다.");
        }
        if (product.getPrice() == null || product.getPrice() < 1000 || product.getPrice() > 1000000) {
            errors.put("price", "가격은 1000원 이상 100만원 이하까지 허용됩니다.");
        }
        if(product.getQuantity() == null || product.getQuantity() < 0 || product.getQuantity() > 9999){
            errors.put("quantity", "수량은 최대 9,999까지 허용됩니다.");
        }

        //검증 로직 - 복합 필드
        if(product.getPrice() != null && product.getQuantity() != null){
            int totalPrice = product.getPrice() * product.getQuantity();

            if (totalPrice < 10000) {
                errors.put("globalError", "총 금액은 10,000원 이상이어야 합니다. 현재 값 = " + totalPrice);
            }
        }

        //검증 실패 시 다시 입력 폼으로
        if (!errors.isEmpty()) {
            log.info("errors = {}", errors);
            model.addAttribute("errors", errors);
            return "validation/v1/addForm";
        }


        Product savedProduct = productRepository.save(product);
        redirectAttributes.addAttribute("id", savedProduct.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v1/products/{id}";
    }


    //상품 수정 폼
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id);
        model.addAttribute("product", product);
        return "validation/v1/editForm";
    }

    //상품 수정 - @PathVariable : @Mapping된 URI 내 'id'에 할당된 값을 갖는다
    @PostMapping("/{id}/edit")
    public String editProduct(@PathVariable Long id, @ModelAttribute Product product, Model model) {
        //검증 오류 결과 보관
        HashMap<String, String> errors = new HashMap<>();

        //검증 로직 - 단일 필드
        if (!StringUtils.hasText(product.getName())) {
            errors.put("name", "상품 이름은 필수입니다.");
        }
        if (product.getPrice() == null || product.getPrice() < 1000 || product.getPrice() > 1000000) {
            errors.put("price", "가격은 1000원 이상 100만원 이하까지 허용됩니다.");
        }
        if(product.getQuantity() == null || product.getQuantity() < 0 || product.getQuantity() > 9999){
            errors.put("quantity", "수량은 최대 9,999까지 허용됩니다.");
        }

        //검증 로직 - 복합 필드
        if(product.getPrice() != null && product.getQuantity() != null){
            int totalPrice = product.getPrice() * product.getQuantity();

            if (totalPrice < 10000) {
                errors.put("globalError", "총 금액은 10,000원 이상이어야 합니다. 현재 값 = " + totalPrice);
            }
        }

        //검증 실패 시 다시 입력 폼으로
        if (!errors.isEmpty()) {
            log.info("errors = {}", errors);
            model.addAttribute("errors", errors);
            return "validation/v1/editForm";
        }

        productRepository.update(id, product);
        return "redirect:/validation/v1/products/{id}";
    }

}
