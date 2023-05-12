package springMVC.productservice.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springMVC.productservice.domain.DeliveryCode;
import springMVC.productservice.domain.Product;
import springMVC.productservice.domain.ProductRepository;
import springMVC.productservice.domain.ProductType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//메시지, 국제화
//메시지 사용시 html에서 반복적으로 사용되는 값들을 한번에 관리 할 수 있음
//크롬 언어 설정에 따라 한국어/영어 버전을 Show
@Slf4j
@Controller
@RequestMapping("/message/products")
@RequiredArgsConstructor
public class MessageProductController {

    private final ProductRepository productRepository;

    //모든 상품 리스트
    @GetMapping
    public String products(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "message/products";
    }

    //상품 상세 정보
    @GetMapping("/{id}")
    public String product(@PathVariable long id, Model model) {
        Product product = productRepository.findById(id);
        model.addAttribute("product", product);
        return "message/product";
    }

    //상품 등록 - 입력 폼 처리
    @GetMapping("/add")
    public String addForm(Model model) {
        //입력할 상품 등록 폼 전달
        model.addAttribute("product", new Product());
        return "message/addForm";
    }

    //상품 추가 : RedirectAttributes : URL 인코딩 수행
    @PostMapping("/add")
    public String addProduct(Product product, RedirectAttributes redirectAttributes) {
        log.info("product.open={}", product.getOpen());

        Product savedProduct = productRepository.save(product);

        redirectAttributes.addAttribute("id", savedProduct.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/message/products/{id}";
    }


    //상품 수정 폼
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id);
        model.addAttribute("product", product);
        return "message/editForm";
    }

    //상품 수정 - @PathVariable : @Mapping된 URI 내 'id'에 할당된 값을 갖는다
    @PostMapping("/{id}/edit")
    public String editProduct(@PathVariable Long id, @ModelAttribute Product product) {
        productRepository.update(id, product);
        return "redirect:/message/products/{id}";
    }

}
