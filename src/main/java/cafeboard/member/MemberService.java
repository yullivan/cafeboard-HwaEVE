package cafeboard.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원 가입
    public MemberResponse registerMember(MemberRequest memberRequest) {
        // Check if the username is already taken
        if (memberRepository.findByUsername(memberRequest.username()).isPresent()) {
            throw new RuntimeException("Username is already taken");
        }

        Member member = new Member(memberRequest.username(), memberRequest.password(), memberRequest.email());
        memberRepository.save(member);
        return new MemberResponse(member.getId(), member.getUsername(), member.getEmail());
    }

    // 회원 탈퇴
    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        memberRepository.delete(member);
    }

    // 로그인 (아이디, 비밀번호 확인)
    public MemberResponse login(String username, String password) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!member.getPassword().equals(password)) {
            throw new RuntimeException("Invalid username or password");
        }

        return new MemberResponse(member.getId(), member.getUsername(), member.getEmail());
    }
}
