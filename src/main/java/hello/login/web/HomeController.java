package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

//    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/")
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
}