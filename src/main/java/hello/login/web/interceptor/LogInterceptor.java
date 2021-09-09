package hello.login.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
// 먼저 HandlerInterceptor를 implements 해준다.
public class LogInterceptor implements HandlerInterceptor {
    public static final String LOG_ID = "logId";
    // 그 후 preHandle, postHandle, afterCompletion을 오버라이드해준다.

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        request.setAttribute(LOG_ID,uuid); // 싱글턴 방식이기 때문에 request에 값을 담아서 afterCompletion에서 꺼내다 쓴다.

        //@RequestMapping : HandlerMethod를 사용
        //정적 리소스(static) : ResourceHttpRequestHandler를 사용

        if(handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod) handler;// 호출할 컨트롤러 메서드의 모든 정보가 포함되어 있다.
        }

        log.info("REQUEST [{}][{}][{}]",uuid,requestURI,handler);
        return true; // false로 두면 컨트롤러 호출 안됌
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle [{}]",modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        Object logId = request.getAttribute(LOG_ID);

        // 예외가 발생해도 afterCompletion은 호출되기 때문에 여기에 종료 로그를 찍어준다.
        log.info("RESPONSE [{}][{}][{}]", logId, requestURI, handler);
        if(ex!=null){
            log.error("afterCompletion error!!", ex);
        }

    }
}
