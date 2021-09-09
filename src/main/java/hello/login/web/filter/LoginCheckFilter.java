package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

// javax.servlet의 Filter
@Slf4j
public class LoginCheckFilter implements Filter {
    // init과 destroy는 default기때문에 굳이 구현하지 않아도 무관하다.
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        Filter.super.init(filterConfig);
//    }

    private static final String[] whitelist = {"/", "/members/add", "/login", "/logout", "/css/*"}; // 로그인하지 않아도 허용할 페이지 리스트

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try{
            log.info("인증 체크 필터 시작 {}",requestURI);
            if(isLoginCheckPath(requestURI)){
                // false라면 로그인인증 시작
                log.info("인증 체크 로직 실행 {}",requestURI);
                HttpSession session = httpRequest.getSession(false);
                if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                    // 로그인되지 않는 사용자 요청
                    log.info("미인증 사용자 요청 {}", requestURI);
                    // 로그인페이지로 리다이렉트
                    // /login?redirectURL="+requestURI 얘는 무엇일까 ?
                    // 로그인페이지로 리다이렉트 하고, 만약 로그인한다면 그전페이지로 돌아가게끔 하는거다.
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
                    return; // 미인증 사용자는 다음으로 진행하지 않고 끝낸다.
                }
            }

            chain.doFilter(request,response);
        }catch (Exception e){
            throw e; // 예외 로깅 가능하지만, 톰켓까지 예외를 보내주어야 한다.
        } finally {
            log.info("인증 체크 필터 종료 {}",requestURI);
        }
    }

    /**
     * 화이트 리스트인경우 인증체크 x
     */

    private boolean isLoginCheckPath(String requestURI){
        return !PatternMatchUtils.simpleMatch(whitelist,requestURI); // whilteList와 requestUri가 맞느냐 ( whiltelist에 안들어가있으면 false )
    }


//    @Override
//    public void destroy() {
//        Filter.super.destroy();
//    }
}
