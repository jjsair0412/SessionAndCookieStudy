package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final SessionManager sessionManager;
    private final MemberRepository memberRepository;

//    @GetMapping("/")
    public String home() {
        return "home";
    }

//    @GetMapping("/")
    public String homeLogin(@CookieValue(name="memberId",required = false)Long memberId, Model model){
        // 쿠키를 받는방법은 여러가지가 있다.
        // 여기서는 스프링에서 제공하는 @CookieValue를 사용한다.
        // name값으로는 아까 로그인에서 쿠키를 생성할 때 지정했던 name을 넣어주면, 저장된 쿠키를 꺼낼수있다.
        // required=false를 주면, 쿠키가 없는 사용자들도 해당 페이지에 접근할 수 있다.
        // 뒤에 쿠키로 받은 값을 저장할 변수를 하나 지정해준다.
        // 그런데 아까 쿠키를 만들때는 String인데, long타입에 저장하고 있다.
        // 이거도 스프링이 알아서 타입컨버팅하여 변환해준다.
        if(memberId==null){
            return "home";
        }
        // 로그인
        Member loginMember = memberRepository.findById(memberId);
        if(loginMember == null){
            return "home";
        }

        // 로그인에 성공햇다면 model에 담아서 html로 넘겨준다.
        model.addAttribute("member",loginMember);
        return "loginHome";
    }

//    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model){
        // 세션 관리자에 저장된 회원정보 조회
        Member member = (Member) sessionManager.getSession(request);

        // 로그인
        if(member == null){
            return "home";
        }

        // 로그인에 성공햇다면 model에 담아서 html로 넘겨준다.
        model.addAttribute("member",member);
        return "loginHome";
    }

//    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);
        if(session == null){
            return "home";
        }

        // session.getAttribute를 사용해서 세션값 꺼내온다.
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 로그인
        if(loginMember == null){
            return "home";
        }

        // 로그인에 성공햇다면 model에 담아서 html로 넘겨준다.
        model.addAttribute("member",loginMember);
        return "loginHome";
    }

    /**
     * 스프링이 제공하는 @SessionAttribute를 사용한다.
     * 얘는 세션을 찾고, 세션에 들어있는 데이터를 찾는 번거로운 과정을 스프링이 한번에 편리하게 처리해준다.
     * getSession과 파라미터는 동일하다. name, required
     */
    @GetMapping("/")
    public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){

        // 로그인
        if(loginMember == null){
            return "home";
        }

        // 로그인에 성공햇다면 model에 담아서 html로 넘겨준다.
        model.addAttribute("member",loginMember);
        return "loginHome";
    }
}