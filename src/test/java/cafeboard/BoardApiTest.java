package cafeboard;

import cafeboard.board.Board;
import cafeboard.board.BoardRepository;
import cafeboard.board.BoardRequestDTO;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BoardApiTest {

    @LocalServerPort
    int port;

    @Autowired
    private BoardRepository boardRepository;

    @BeforeEach
    void setUp() {
        // 데이터베이스 초기화
        boardRepository.deleteAll();
        RestAssured.port = port;
    }

    private Long createBoardAndReturnId(String boardName) {
        Board board = new Board(boardName);
        boardRepository.save(board);
        return board.getId();
    }

    @Test
    void 게시판목록조회() {
        RestAssured.given().log().all()
                .when()
                .get("/boards")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 게시판상세조회() {
        Long boardId = createBoardAndReturnId("새 게시판");

        RestAssured.given().log().all()
                .pathParam("id", boardId)
                .when()
                .get("/boards/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(boardId.intValue()));
    }

    @Test
    void 게시판생성() {
        BoardRequestDTO boardRequestDto = new BoardRequestDTO("새 게시판");

        RestAssured.given().log().all()
                .contentType("application/json")
                .body(boardRequestDto)
                .when()
                .post("/boards")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo(boardRequestDto.name()))
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());
    }

    @Test
    void 게시판수정() {
        Long boardId = createBoardAndReturnId("기존 게시판");

        BoardRequestDTO updatedBoardRequestDto = new BoardRequestDTO("수정된 게시판");

        RestAssured.given().log().all()
                .contentType("application/json")
                .body(updatedBoardRequestDto)
                .pathParam("id", boardId)
                .when()
                .put("/boards/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo(updatedBoardRequestDto.name()));

        Board updatedBoard = boardRepository.findById(boardId).orElseThrow();
        assertThat(updatedBoard.getName()).isEqualTo(updatedBoardRequestDto.name());
    }

    @Test
    void 게시판삭제() {
        Long boardId = createBoardAndReturnId("삭제할 게시판");

        // 삭제 요청
        RestAssured.given().log().all()
                .pathParam("id", boardId)
                .when()
                .delete("/boards/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // 삭제 후 조회 시 404 응답 확인
        RestAssured.given().log().all()
                .pathParam("id", boardId)
                .when()
                .get("/boards/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
