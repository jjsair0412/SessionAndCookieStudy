package hello.login;

import hello.login.web.argumentresolver.LoginMemberArgumentResolver;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

@Configuration
// 스프링 인터셉터를 사용하기 위해선, WebMvcConfigurer를 implements해주어야 한다.
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    // 그 후 addInterceptors라는것을 오버라이드 해준다.
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 그리고 registry에 addInterceptor를 해준후 인자값으로 만들어준 인터셉터를 넣어준다.
        registry.addInterceptor(new LogInterceptor())
                .order(1) //체인형식이기 때문에 순서를 정해준다. 낮은숫자가 먼저 실행된다
                .addPathPatterns("/**") // 그 후 적용할 url을 선택한다. "/**"이렇게 적어주면 /의 하위는 전부다 적용이라는 의미이다.
                .excludePathPatterns("/css**","/*.ico","/error"); // excludePathPatterns()에는 인터셉터를 적용하지 않을 페이지를 적어준다.


        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**") // 기본적으로 모든 경로에 인터셉터를 적용하되, 예외를 둘 수 있다.
                .excludePathPatterns("/","/members/add","/login","/logout"
                        ,"/css/**","/*.ico","/error");
        // 스프링 인터셉터에서는 여기서 어떤 페이지에 인터셉터를 안거는곳을 한번에 처리할 수 있다.
        // 그러나 필터도 해당 기능을 구현하기 위한 로직이 필요했다.
    }


    // 스프링부트에선 FilterReqistrationBean을 bean생성해주어야 한다.
//    @Bean
    public FilterRegistrationBean logFiter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter()); // 만들어준 필터 넣어주기 ( 등록할 필터 지정하기 )
        filterRegistrationBean.setOrder(1); // 필터 순서 정해주기. 필터가 체인으로 여러개 들어갈 수 있기 때문에 ( 숫자가 낮을수록 먼저 동작한다. )
        filterRegistrationBean.addUrlPatterns("/*"); // 적용할 url 선택. "/*" 이렇게해주면 모든 url에 적용한다.
        return filterRegistrationBean; // 반환
    }

//    @Bean
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
