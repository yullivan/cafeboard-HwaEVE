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
    public BoardResponseDTO createBoard(BoardRequestDTO boardRequestDTO) {
        Board board = new Board(boardRequestDTO.name());
        board = boardRepository.save(board);
        return new BoardResponseDTO(board.getId(), board.getName(), board.getCreatedAt(), board.getUpdatedAt());
    }

    // Read all
    public List<BoardResponseDTO> getAllBoards() {
        return boardRepository.findAll().stream()
                .map(board -> new BoardResponseDTO(board.getId(), board.getName(), board.getCreatedAt(), board.getUpdatedAt()))
                .toList();
    }

    // Read one
    public Optional<BoardResponseDTO> getBoardById(Long id) {
        return boardRepository.findById(id)
                .map(board -> new BoardResponseDTO(board.getId(), board.getName(), board.getCreatedAt(), board.getUpdatedAt()));
    }

    // Update
    public Optional<BoardResponseDTO> updateBoard(Long id, BoardRequestDTO boardRequestDTO) {
        return boardRepository.findById(id).map(board -> {
            board.setName(boardRequestDTO.name());
            boardRepository.save(board);
            return new BoardResponseDTO(board.getId(), board.getName(), board.getCreatedAt(), board.getUpdatedAt());
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
