package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.http.HttpResponse;

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form){
        return "login/loginForm";
    }

    @PostMapping("login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult , HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if(loginMember == null){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }
        // 로그인 성공 처리 TODO
        // 쿠키를 new키워드로 생성
        // 인자값으로는 파라미터에 들아갈 name과 값이 순서대로 들어간다.
        // 해당 예제에서는 memberId의 이름으로 value가 loginMember.getId가 들어간다. 또한 시간을 설정해주지 않았기 때문에 영속 쿠키이다.
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));

        //그 후 HttpServletResponse에 addCookie를 사용해서 만들어준 쿠키를 집어넣는다.
        response.addCookie(idCookie);
        // 이렇게해주면 웹 브라우저는 종료전가지 서버에 쿠키를 계속 보내줄 것이다.


        return "redirect:/";
    }

    @PostMapping("logout")
    public String logout(HttpServletResponse response){
        expireCookie(response, "memberId");
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
