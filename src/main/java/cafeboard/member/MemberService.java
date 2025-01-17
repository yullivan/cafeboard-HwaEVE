package cafeboard.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    // 회원 가입
    public MemberResponse registerMember(MemberRequest memberRequest) {
        // Check if the username is already taken
        if (memberRepository.findByUsername(memberRequest.username()).isPresent()) {
            throw new RuntimeException("Username is already taken");
        }

        // 비밀번호 해싱
        String hashedPassword = SecurityUtils.sha256EncryptBase64(memberRequest.password());

        // 해싱된 비밀번호로 새로운 회원 생성
        Member member = new Member(memberRequest.username(), hashedPassword, memberRequest.email());
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
    public String login(String username, String password) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        // 입력된 비밀번호를 해싱하여 저장된 해시와 비교
        String hashedInputPassword = SecurityUtils.sha256EncryptBase64(password);

        // 비밀번호가 일치하는지 확인
        if (!member.getPassword().equals(hashedInputPassword)) {
            throw new RuntimeException("Invalid username or password");
        }

        // JWT 토큰 생성 후 반환
        return jwtProvider.createToken(username); // 또는 이메일을 담을 수 있음
    }
}
