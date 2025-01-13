package cafeboard.post;

import cafeboard.board.Board;
import cafeboard.board.BoardRepository;
import cafeboard.comment.Comment;
import cafeboard.comment.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository; // BoardRepository 주입

    public PostService(PostRepository postRepository, CommentRepository commentRepository, BoardRepository boardRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
    }

    // 게시글 생성
    public PostResponse createPost(PostRequest postRequest, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        Post post = new Post(postRequest.title(), postRequest.content(), board);
        postRepository.save(post);
        return toPostResponse(post);
    }

    // 게시글 목록 조회 (댓글 개수 포함)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::toPostResponse)
                .collect(Collectors.toList());
    }

    // 게시글 상세 조회 (댓글 목록 포함)
    public PostDetailResponse getPostDetail(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        List<CommentResponse> comments = commentRepository.findByPostId(id).stream()
                .map(this::toCommentResponse)
                .collect(Collectors.toList());
        return new PostDetailResponse(post.getId(), post.getTitle(), post.getContent(),
                post.getCreatedAt(), post.getUpdatedAt(), comments);
    }

    // 게시글 수정
    @Transactional
    public PostResponse updatePost(Long id, PostRequest postRequest) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.update(postRequest.title(), postRequest.content());
        return toPostResponse(post);
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        postRepository.delete(post);
    }

    private PostResponse toPostResponse(Post post) {
        int commentCount = commentRepository.findByPostId(post.getId()).size();
        return new PostResponse(post.getId(), post.getTitle(), post.getContent(),
                post.getCreatedAt(), post.getUpdatedAt(), commentCount);
    }

    private CommentResponse toCommentResponse(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getContent(), comment.getCreatedAt(), comment.getUpdatedAt());
    }
}
