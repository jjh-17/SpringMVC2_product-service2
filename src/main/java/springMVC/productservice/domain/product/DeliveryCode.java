package springMVC.productservice.domain.product;

import lombok.AllArgsConstructor;
import lombok.Data;

//배송 코드: FAST, NORMAL, SLOW
@Data
@AllArgsConstructor
public class DeliveryCode {
    private String code;        //시스템에서 전달하는 값
    private String displayName; //고객에게 보여주는 정보
}
