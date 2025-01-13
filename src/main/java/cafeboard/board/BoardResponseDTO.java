package cafeboard.board;

import java.time.LocalDateTime;

public record BoardResponseDTO(Long id, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
