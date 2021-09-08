package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;

    /**
     * @return이 null이면 로그인 실패
     */
    public Member login(String loginid, String password){
//        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginid);
//        Member member = findMemberOptional.get();
//        if(member.getPassword().equals(password)){
//            return member;
//        }else{
//            return null;
//        }

        return memberRepository.findByLoginId(loginid) // optional로 받아온 값은 filter를 사용할 수 있다
                .filter(m -> m.getPassword().equals(password)) // filter라는것을 사용해서, 받아온 값과 비교해주는 구문을 람다식으로 변경할 수 있따.
                .orElse(null); // else문이랑 같은 역할
    }
}
