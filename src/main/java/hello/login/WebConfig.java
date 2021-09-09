package hello.login;

import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class WebConfig {

    // 스프링부트에선 FilterReqistrationBean을 bean생성해주어야 한다.
    @Bean
    public FilterRegistrationBean logFiter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter()); // 만들어준 필터 넣어주기 ( 등록할 필터 지정하기 )
        filterRegistrationBean.setOrder(1); // 필터 순서 정해주기. 필터가 체인으로 여러개 들어갈 수 있기 때문에 ( 숫자가 낮을수록 먼저 동작한다. )
        filterRegistrationBean.addUrlPatterns("/*"); // 적용할 url 선택. "/*" 이렇게해주면 모든 url에 적용한다.
        return filterRegistrationBean; // 반환
    }

    @Bean
    public FilterRegistrationBean loginCehckFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/*");
        // 화이트리스트를 통해 로그인을 체크하지 않는 페이지를 만들어주엇기 때문에, 여기서 걸러주지 않아도 된다.
        // 이렇게 개발하면, 미래에 어떤 html페이지가 만들어진다 하더라도 영향을 받지 않는다.
        // 여기서 다 걸러주면 페이지를 추가할 때 마다 URlPatterns에 넣어주어야 한다.
        return filterRegistrationBean;
    }
}
