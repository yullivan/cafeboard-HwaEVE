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
    public ResponseEntity<BoardResponse> createBoard(@RequestBody BoardRequest boardRequest) {
        BoardResponse boardResponse = boardService.createBoard(boardRequest);
        return new ResponseEntity<>(boardResponse, HttpStatus.CREATED);
    }

    // Read all
    @GetMapping("/boards")
    public ResponseEntity<List<BoardResponse>> getAllBoards() {
        List<BoardResponse> boards = boardService.getAllBoards();
        return new ResponseEntity<>(boards, HttpStatus.OK);
    }

    // Read one
    @GetMapping("/boards/{id}")
    public ResponseEntity<BoardResponse> getBoardById(@PathVariable Long id) {
        Optional<BoardResponse> board = boardService.getBoardById(id);
        return board.map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update
    @PutMapping("/boards/{id}")
    public ResponseEntity<BoardResponse> updateBoard(@PathVariable Long id, @RequestBody BoardRequest boardRequest) {
        Optional<BoardResponse> updatedBoard = boardService.updateBoard(id, boardRequest);
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
