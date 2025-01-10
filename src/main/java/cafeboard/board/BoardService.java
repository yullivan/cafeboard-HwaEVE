package cafeboard.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public BoardResponseDto createBoard(BoardRequestDto requestDto) {
        Board board = new Board();
        board.setName(requestDto.name());
        boardRepository.save(board);
        return new BoardResponseDto(board.getId(), board.getName(), board.getCreatedAt(), board.getUpdatedAt());
    }

    public List<BoardResponseDto> getAllBoards() {
        return boardRepository.findAll().stream()
                .map(board -> new BoardResponseDto(board.getId(), board.getName(), board.getCreatedAt(), board.getUpdatedAt()))
                .toList();
    }

    public Optional<BoardResponseDto> getBoardById(Long id) {
        Optional<Board> board = boardRepository.findById(id);
        return board.map(b -> new BoardResponseDto(b.getId(), b.getName(), b.getCreatedAt(), b.getUpdatedAt()));
    }

    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Board not found"));
        board.setName(requestDto.name());
        boardRepository.save(board);
        return new BoardResponseDto(board.getId(), board.getName(), board.getCreatedAt(), board.getUpdatedAt());
    }

    public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Board not found"));
        boardRepository.delete(board);
    }
}
