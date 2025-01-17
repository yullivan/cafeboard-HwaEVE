package cafeboard.board;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // Create
    public BoardResponse createBoard(BoardRequest boardRequest) {
        Board board = new Board(boardRequest.name());
        board = boardRepository.save(board);
        return new BoardResponse(board.getId(), board.getName(), board.getCreatedAt(), board.getUpdatedAt());
    }

    // Read all
    public List<BoardResponse> getAllBoards() {
        return boardRepository.findAll().stream()
                .map(board -> new BoardResponse(board.getId(), board.getName(), board.getCreatedAt(), board.getUpdatedAt()))
                .toList();
    }

    // Read one
    public Optional<BoardResponse> getBoardById(Long id) {
        return boardRepository.findById(id)
                .map(board -> new BoardResponse(board.getId(), board.getName(), board.getCreatedAt(), board.getUpdatedAt()));
    }

    // Update
    public Optional<BoardResponse> updateBoard(Long id, BoardRequest boardRequest) {
        return boardRepository.findById(id).map(board -> {
            board.setName(boardRequest.name());
            boardRepository.save(board);
            return new BoardResponse(board.getId(), board.getName(), board.getCreatedAt(), board.getUpdatedAt());
        });
    }

    // Delete
    public boolean deleteBoard(Long id) {
        return boardRepository.findById(id).map(board -> {
            boardRepository.delete(board);
            return true;
        }).orElse(false);
    }
}
