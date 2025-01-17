package cafeboard.member;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원 가입
    @PostMapping("/members/register")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse registerMember(@RequestBody MemberRequest memberRequest) {
        return memberService.registerMember(memberRequest);
    }

    // 회원 탈퇴
    @DeleteMapping("/members/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
    }

    // 로그인
    @PostMapping("/members/login")
    public MemberResponse login(@RequestParam String username, @RequestParam String password) {
        return memberService.login(username, password);
    }
}
