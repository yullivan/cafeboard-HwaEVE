package cafeboard.comment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 생성
    @PostMapping("/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse createComment(@PathVariable Long postId, @RequestBody CommentRequest commentRequest) {
        return commentService.createComment(postId, commentRequest);
    }

    // 댓글 수정
    @PutMapping("/posts/{postId}/comments/{id}")
    public CommentResponse updateComment(@PathVariable Long id, @RequestBody CommentRequest commentRequest) {
        return commentService.updateComment(id, commentRequest);
    }

    @DeleteMapping("/posts/{postId}/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }

    @GetMapping("/posts/{postId}/comments/{id}")
    public CommentResponse getComment(@PathVariable Long postId, @PathVariable Long id) {
        return commentService.getComment(id);
    }

    // 댓글 목록 조회
    @GetMapping("/posts/{postId}/comments")
    public List<CommentResponse> getComments(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId);
    }
}
