package springMVC.productservice.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springMVC.productservice.domain.product.DeliveryCode;
import springMVC.productservice.domain.product.Product;
import springMVC.productservice.domain.product.ProductRepository;
import springMVC.productservice.domain.product.ProductType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//상품 목록 컨트롤러
@Slf4j
@Controller
@RequestMapping("/form/products")
@RequiredArgsConstructor
public class BasicProductController {

    private final ProductRepository productRepository;

    //모든 상품 리스트
    @GetMapping
    public String products(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "form/products";
    }

    //상품 상세 정보
    @GetMapping("/{id}")
    public String product(@PathVariable long id, Model model) {
        Product product = productRepository.findById(id);
        model.addAttribute("product", product);
        return "form/product";
    }

    //상품 등록 - 입력 폼 처리
    @GetMapping("/add")
    public String addForm(Model model) {
        //입력할 상품 등록 폼 전달
        model.addAttribute("product", new Product());
        return "form/addForm";
    }

    //상품 추가 : RedirectAttributes : URL 인코딩 수행
    @PostMapping("/add")
    public String addProduct(Product product, RedirectAttributes redirectAttributes) {
        log.info("product.open={}", product.getOpen());

        Product savedProduct = productRepository.save(product);

        redirectAttributes.addAttribute("id", savedProduct.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/products/{id}";
    }


    //상품 수정 폼
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id);
        model.addAttribute("product", product);
        return "form/editForm";
    }

    //상품 수정 - @PathVariable : @Mapping된 URI 내 'id'에 할당된 값을 갖는다
    @PostMapping("/{id}/edit")
    public String editProduct(@PathVariable Long id, @ModelAttribute Product product) {
        productRepository.update(id, product);
        return "redirect:/form/products/{id}";
    }

    @ModelAttribute("regions") //컨트롤러 호출 시 'regions'에 자동 addAttribute 수행
    public Map<String, String> regions() {
        LinkedHashMap<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("JEJU", "제주");
        regions.put("BUSAN", "부산");
        regions.put("gyeonggi", "경기");

        return regions;
    }

    @ModelAttribute("productTypes")
    public ProductType[] productTypes() {
        return ProductType.values();
    }

    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCode() {
        ArrayList<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
        return deliveryCodes;
    }
}
