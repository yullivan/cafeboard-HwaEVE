package cafeboard.post;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        int commentCount) {
}
