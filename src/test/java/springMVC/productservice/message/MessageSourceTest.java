package springMVC.productservice.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

@SpringBootTest
public class MessageSourceTest {

    @Autowired
    MessageSource messageSource;

    //메시지가 있는 경우
    @Test
    public void helloMessage() throws Exception {
        //given
        //code: hello, args: null, locale: null ==> locale 미설정 시 기본 메시지 파일에서 데이터 조회
        String simpleMessage = messageSource.getMessage("hello", null, null);

        //when

        //then
        Assertions.assertThat(simpleMessage).isEqualTo("안녕");
        System.out.println("메시지가 존재한다.");

    }

    //메시지가 없는 경우
    @Test
    public void notFoundMessageCode() throws Exception {
        //given

        //when

        //then
        Assertions.assertThatThrownBy(() -> messageSource.getMessage("no_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);
        System.out.println("메시지가 존재하지 않는다.");
    }
}
