package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

// import javax.servlet.Filter;
// javax의 필터를 implements해준다.
@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init"); // 초기화
    }

    // HTTP 요청이 오면 doFilter 생성
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter"); // 필터등록

        // ServletRequest는 HttpServletRequest의 부모 인터페이스이다.
        // 따라서 ServletRequest를 그냥 사용하면 안돼고, 다운 케스팅을 통해 HttpServletRequest를 사용해야 한다.
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        // 요청온 사용자를 구분하기 위해 uuid 생성
        String uuid = UUID.randomUUID().toString();

        try{
            log.info("REQUEST [{}][{}]",uuid,requestURI);
            // 꼭 chain을 사용해서 다음 필터를 호출해야 한다.
            // 여기서 다음필터가 존재 하면 다음 필터를 호출하고, 존재하지 않으면 그냥 서블릿을 호출한다.
            // chain.doFilter를 안써주면 다음단계로 진행되지 않는다. 서블릿이 아예 호출되지 않는다.
            chain.doFilter(request,response);
        }catch (Exception e){
            throw e;
        }finally {
            log.info("RESPONSE [{}][{}]",uuid,requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy"); // 필터제거
    }
}
