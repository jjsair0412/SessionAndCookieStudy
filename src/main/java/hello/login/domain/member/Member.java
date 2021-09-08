package hello.login.domain.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Member {
    private Long id;

    @NotEmpty
    private String loginId; // 로그인 id
    @NotEmpty
    private String name; // 사용자 이름
    @NotEmpty
    private String password; // 사용자 비밀번호
}
