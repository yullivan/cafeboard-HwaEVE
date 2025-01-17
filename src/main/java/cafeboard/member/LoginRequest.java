package cafeboard.member;

public record LoginRequest(
        String email,
        String password
) {
}
