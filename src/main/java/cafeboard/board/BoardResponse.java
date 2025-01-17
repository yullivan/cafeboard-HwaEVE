package cafeboard.board;

import java.time.LocalDateTime;

public record BoardResponse(Long id, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
