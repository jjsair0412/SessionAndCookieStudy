package hello.login.web.session;

import hello.login.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;


import static org.assertj.core.api.Assertions.assertThat;

class SessionManagerTest {
    SessionManager sessionManager = new SessionManager();

    @Test
    void SessionTest(){
        // http 관련 인터페이스를 활용해 테스트를 진행하고싶을땐 Mock 객체를 사용한다.
        MockHttpServletResponse response = new MockHttpServletResponse();
        // 세션생성 - 서버에서 클라이언트로
        Member member = new Member();
        sessionManager.createSession(member, response);

        // 요청에 응답 쿠키 저장 - 클라이언트에서 서버로
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());


        // 세션 조회
        Object result = sessionManager.getSession(request);
        assertThat(result).isEqualTo(member);

        //세션 만료
        sessionManager.expire(request);
        Object expired = sessionManager.getSession(request);
        assertThat(expired).isNull();

    }
}