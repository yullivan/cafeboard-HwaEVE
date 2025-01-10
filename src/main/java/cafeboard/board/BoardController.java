package cafeboard.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 게시판 생성
    @PostMapping("/boards")
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto requestDto) {
        BoardResponseDto board = boardService.createBoard(requestDto);
        return new ResponseEntity<>(board, HttpStatus.CREATED);
    }

    // 전체 게시판 조회
    @GetMapping("/boards")
    public ResponseEntity<List<BoardResponseDto>> getAllBoards() {
        List<BoardResponseDto> boards = boardService.getAllBoards();
        return new ResponseEntity<>(boards, HttpStatus.OK);
    }

    // 게시판 조회 (ID로 조회)
    @GetMapping("/boards/{id}")
    public ResponseEntity<BoardResponseDto> getBoardById(@PathVariable("id") Long id) {
        Optional<BoardResponseDto> board = boardService.getBoardById(id);
        return board.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // 게시판 수정
    @PutMapping("/boards/{id}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable("id") Long id, @RequestBody BoardRequestDto requestDto) {
        BoardResponseDto updatedBoard = boardService.updateBoard(id, requestDto);
        return ResponseEntity.ok(updatedBoard);
    }

    // 게시판 삭제
    @DeleteMapping("/boards/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable("id") Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }
}
