package cafeboard.board;

import java.time.LocalDateTime;

public record BoardResponseDto(
        Long id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
