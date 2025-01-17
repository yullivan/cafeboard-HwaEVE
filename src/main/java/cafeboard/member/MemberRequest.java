// MemberRequest: 회원 가입 시 필요한 데이터
package cafeboard.member;

public record MemberRequest(String username, String password, String email) {}

