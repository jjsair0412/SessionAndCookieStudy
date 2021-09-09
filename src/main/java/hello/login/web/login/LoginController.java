package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.http.HttpResponse;

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {
    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form){
        return "login/loginForm";
    }

//    @PostMapping("login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult , HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if(loginMember == null){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }
        // 쿠키를 new키워드로 생성
        // 인자값으로는 파라미터에 들아갈 name과 값이 순서대로 들어간다.
        // 해당 예제에서는 memberId의 이름으로 value가 loginMember.getId가 들어간다. 또한 시간을 설정해주지 않았기 때문에 영속 쿠키이다.
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));

        //그 후 HttpServletResponse에 addCookie를 사용해서 만들어준 쿠키를 집어넣는다.
        response.addCookie(idCookie);
        // 이렇게해주면 웹 브라우저는 종료전가지 서버에 쿠키를 계속 보내줄 것이다.

        return "redirect:/";
    }

//    @PostMapping("login")
    public String loginV2(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult , HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if(loginMember == null){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }
        // 세션 관리자를 통해 세션을 생성하고 , 회원 데이터를 보관한다.
        sessionManager.createSession(loginMember,response);

        return "redirect:/";
    }

    /**
     * Http세션 처리
     */
//    @PostMapping("login")
    public String loginV3(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult , HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if(loginMember == null){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }
        // 세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        // HttpServletRequest의 getSession()을 통해서 HttpSession을 생성해준다.
        // 세션에 getSession()인자값으로는  true, false라는 두가지 옵션이 존재한다.
        // 세션을 생성하기위해선 getSession에 true라는 파라미터를 넣어주면 된다.
        //HttpSession session = request.getSession(true);
        // 디폴트값은 ture기 때문에, ture옵션을 사용하기 위해서는 그냥 생략해도 무관하다.
        HttpSession session = request.getSession();
        // 세션에 로그인 회원 정보를 보관한다.
        // setAttribute의 파라미터로는 순서대로 세션이름, 세션에 들어갈 값이 들어간다.
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);

        return "redirect:/";
    }

    @PostMapping("login")
    public String loginV4(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
                          // requestParam으로 같이 넘어온 url을 받아준다.
                          @RequestParam(defaultValue = "/") String redirectURL,
                          HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if(loginMember == null){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);

        return "redirect:"+redirectURL;
    }

//    @PostMapping("logout")
    public String logout(HttpServletResponse response){
        expireCookie(response, "memberId");
        return "redirect:/";
    }

//    @PostMapping("logout")
    public String logoutV2(HttpServletRequest request){
        sessionManager.expire(request);
        return "redirect:/";
    }

    @PostMapping("logout")
    public String logoutV3(HttpServletRequest request){
        HttpSession session = request.getSession(false); // getSession을 false로 가져온다. true면 무조건 만들기 떄문에
        if(session!=null){
            // invalidate를 사용하면 세션에잇는 값이 없어진다.
            session.invalidate();
        }
        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        // 쿠키를 날려버리는 방법은 , 쿠키 유효시간을 null로 만들면 된다.
        Cookie cookie = new Cookie(cookieName, null);
        // setMaxAge를 0으로 설정하고
        cookie.setMaxAge(0);
        // 쿠키를 다시 HttpServletResponse에 add해주면 된다.
        response.addCookie(cookie);
    }
}
