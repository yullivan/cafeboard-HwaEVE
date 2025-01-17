package cafeboard.comment;

import cafeboard.post.Post;
import cafeboard.post.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    // 댓글 생성
    public CommentResponse createComment(Long postId, CommentRequest commentRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        Comment comment = new Comment(post, commentRequest.content());
        commentRepository.save(comment);
        return toCommentResponse(comment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponse updateComment(Long id, CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.update(commentRequest.content());
        return toCommentResponse(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        commentRepository.delete(comment);
    }

    // 댓글 조회
    public CommentResponse getComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        return toCommentResponse(comment);
    }

    // 댓글 목록 조회
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(this::toCommentResponse)
                .collect(Collectors.toList());
    }

    private CommentResponse toCommentResponse(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getContent(), comment.getCreatedAt(), comment.getUpdatedAt());
    }
}
