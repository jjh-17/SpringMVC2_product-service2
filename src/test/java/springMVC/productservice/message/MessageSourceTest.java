package springMVC.productservice.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MessageSourceTest {

    @Autowired
    MessageSource messageSource;

    //메시지가 있는 경우
    @Test
    public void helloMessage() throws Exception {
        //given
        //code: hello, args: null, locale: null
        //locale이 null ==> 시스템 기본 locale(locale.getDefault())로 저회 시도
        //             ==> 조회 실패 시 messages.properties 조회
        String simpleMessage = messageSource.getMessage("hello", null, null);

        //when

        //then
        assertThat(simpleMessage).isEqualTo("안녕");
        System.out.println("메시지가 존재한다.");

    }

    //메시지가 없는 경우
    @Test
    public void notFoundMessageCode() throws Exception {
        //given

        //when
        assertThatThrownBy(() -> messageSource.getMessage("no_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);
        
        //then
        System.out.println("메시지가 존재하지 않는다.");
    }

    //메시지가 없는 경우
    @Test
    public void notFoundMessageCodeDefault() throws Exception {
        //given
        String result = messageSource.getMessage("no_code", null, "기본 메시지", null);

        //when
        assertThat(result).isEqualTo("기본 메시지");

        //then
        System.out.println("메시지가 존재하지 않아 default 메시지 출력.");
    }

    //메시지 매개변수 사용
    @Test
    public void argumentMessage() throws Exception {
        //given
        String message = messageSource.getMessage("hello.name", new Object[]{"Spring"}, null);

        //when
        assertThat(message).isEqualTo("안녕 Spring");

        //then
        System.out.println("메시지 매개변수 사용 성공");
    }

    //국제화 파일 선택
    @Test
    public void languageMessage() throws Exception {
        //given

        //when
        assertThat(messageSource.getMessage("hello", null, null)).isEqualTo("안녕");
        System.out.println("default 메시지 사용 완료");

        assertThat(messageSource.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
        System.out.println("Locale.KOREA 메시지 사용 완료");

        assertThat(messageSource.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
        System.out.println("Locale.ENGLISH 메시지 사용 완료");

    }
}
