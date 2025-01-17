package cafeboard.comment;

import cafeboard.post.Post;
import cafeboard.post.PostRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentApiTest {

    @LocalServerPort
    int port;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        // 데이터베이스 초기화
        commentRepository.deleteAll();
        postRepository.deleteAll();
        RestAssured.port = port;
    }

    // 게시글 생성 후 ID 반환
    private Long createPostAndReturnId(String content) {
        Post post = new Post(content); // 게시글 생성
        postRepository.save(post); // 게시글 저장
        return post.getId(); // 생성된 게시글 ID 반환
    }

    // 댓글 생성 후 ID 반환
    private Long createCommentAndReturnId(Long postId, String content) {
        CommentRequest commentRequest = new CommentRequest(content);
        CommentResponse commentResponse = RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(commentRequest)
                .pathParam("postId", postId) // 경로 파라미터에 postId 전달
                .when()
                .post("/posts/{postId}/comments")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(CommentResponse.class);
        return commentResponse.id(); // 생성된 댓글 ID 반환
    }

    // 댓글 생성 테스트
    @Test
    void 댓글생성() {
        Long postId = createPostAndReturnId("새 게시물");

        CommentRequest commentRequest = new CommentRequest("새 댓글");

        CommentResponse response = RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(commentRequest)
                .pathParam("postId", postId)  // pathParam 사용하여 postId 전달
                .when()
                .post("/posts/{postId}/comments")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(CommentResponse.class);

        assertThat(response.content()).isEqualTo(commentRequest.content());
    }

    // 댓글 수정 테스트
    @Test
    void 댓글수정() {
        Long postId = createPostAndReturnId("새 게시물");
        Long commentId = createCommentAndReturnId(postId, "기존 댓글");

        CommentRequest updatedCommentRequest = new CommentRequest("수정된 댓글");

        CommentResponse updatedCommentResponse = RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(updatedCommentRequest)
                .pathParam("postId", postId)  // pathParam 사용하여 postId 전달
                .pathParam("id", commentId)   // pathParam 사용하여 commentId 전달
                .when()
                .put("/posts/{postId}/comments/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(CommentResponse.class);

        assertThat(updatedCommentResponse.content()).isEqualTo(updatedCommentRequest.content());
    }

    // 댓글 삭제 테스트
    @Test
    void 댓글삭제() {
        Long postId = createPostAndReturnId("새 게시물");
        Long commentId = createCommentAndReturnId(postId, "삭제할 댓글");

        // 댓글 삭제 요청
        RestAssured.given()
                .log().all()
                .pathParam("postId", postId)  // pathParam 사용하여 postId 전달
                .pathParam("id", commentId)   // pathParam 사용하여 commentId 전달
                .when()
                .delete("/posts/{postId}/comments/{id}")  // DELETE 경로 확인
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());  // 204 No Content 응답

        // 삭제 후 댓글 조회 시 404 응답 확인
        RestAssured.given()
                .log().all()
                .pathParam("postId", postId)  // pathParam 사용하여 postId 전달
                .pathParam("id", commentId)   // pathParam 사용하여 commentId 전달
                .when()
                .get("/posts/{postId}/comments/{id}")  // GET 경로 확인
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());  // 404 Not Found 응답
    }


    // 댓글 목록 조회 테스트
    @Test
    void 댓글목록조회() {
        Long postId = createPostAndReturnId("새 게시물");

        // 댓글 2개 생성
        createCommentAndReturnId(postId, "첫 번째 댓글");
        createCommentAndReturnId(postId, "두 번째 댓글");

        RestAssured.given()
                .log().all()
                .pathParam("postId", postId)  // pathParam 사용하여 postId 전달
                .when()
                .get("/posts/{postId}/comments")  // 댓글 목록 조회
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(2));  // 댓글 개수 확인
    }
}
