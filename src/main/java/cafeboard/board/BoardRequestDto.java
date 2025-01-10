package cafeboard.board;

import jakarta.validation.constraints.NotBlank;

public record BoardRequestDto(@NotBlank String name) {
}
