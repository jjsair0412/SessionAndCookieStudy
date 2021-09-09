package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null){
            return "세션이 없습니다.";
        }
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name={}, value={}",name,session.getAttribute(name)));
        log.info("sessionId={}",session.getId()); // 세션Id 꺼내오기
        log.info("getMaxInactiveInterval={}",session.getMaxInactiveInterval()); // 세션의 유효 시간, 예) 1800초, (30분)
        log.info("creationTime={}",new Date(session.getCreationTime())); // 세션 생성 일시
        log.info("lastAccessedTime={}",new Date(session.getLastAccessedTime())); // 사용자가 해당 세션에 마지막으로 접근한 시간
        log.info("isNew={}",session.isNew()); // 새로 생성된 세션인지, 아니면 이미 과거에 만들어졌고, 클라이언트에서 서버로 sessionId ( JSESSIONID )를 요청해서 조회된 세션인지 여부
        return "세션 출력";
    }
}
