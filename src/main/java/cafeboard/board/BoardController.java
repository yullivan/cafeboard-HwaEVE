package cafeboard.board;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // Create
    @PostMapping("/boards")
    public ResponseEntity<BoardResponseDTO> createBoard(@RequestBody BoardRequestDTO boardRequestDTO) {
        BoardResponseDTO boardResponse = boardService.createBoard(boardRequestDTO);
        return new ResponseEntity<>(boardResponse, HttpStatus.CREATED);
    }

    // Read all
    @GetMapping("/boards")
    public ResponseEntity<List<BoardResponseDTO>> getAllBoards() {
        List<BoardResponseDTO> boards = boardService.getAllBoards();
        return new ResponseEntity<>(boards, HttpStatus.OK);
    }

    // Read one
    @GetMapping("/boards/{id}")
    public ResponseEntity<BoardResponseDTO> getBoardById(@PathVariable Long id) {
        Optional<BoardResponseDTO> board = boardService.getBoardById(id);
        return board.map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update
    @PutMapping("/boards/{id}")
    public ResponseEntity<BoardResponseDTO> updateBoard(@PathVariable Long id, @RequestBody BoardRequestDTO boardRequestDTO) {
        Optional<BoardResponseDTO> updatedBoard = boardService.updateBoard(id, boardRequestDTO);
        return updatedBoard.map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete
    @DeleteMapping("/boards/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boolean deleted = boardService.deleteBoard(id);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
