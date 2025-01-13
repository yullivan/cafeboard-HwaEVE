package cafeboard.post;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse createPost(@RequestBody PostRequest postRequest, @RequestParam Long boardId) {
        return postService.createPost(postRequest, boardId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/posts/{id}")
    public PostDetailResponse getPostDetail(@PathVariable Long id) {
        return postService.getPostDetail(id);
    }

    @PutMapping("/posts/{id}")
    public PostResponse updatePost(@PathVariable Long id, @RequestBody PostRequest postRequest) {
        return postService.updatePost(id, postRequest);
    }

    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }
}
