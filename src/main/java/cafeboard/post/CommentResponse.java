package cafeboard.post;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
