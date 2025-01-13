package cafeboard;

import cafeboard.board.Board;
import cafeboard.board.BoardRepository;
import cafeboard.board.BoardRequestDTO;
import cafeboard.board.BoardResponseDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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

        BoardResponseDTO response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(boardRequestDto)
                .when()
                .post("/boards")
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(BoardResponseDTO.class);

        assertThat(response.name()).isEqualTo(boardRequestDto.name());
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.updatedAt()).isNotNull();
    }

    @Test
    void 게시판수정() {
        // 게시판 생성 후 ID 받기
        Long boardId = createBoardAndReturnId("기존 게시판");

        // 수정할 게시판 데이터
        BoardRequestDTO updatedBoardRequestDto = new BoardRequestDTO("수정된 게시판");

        // 게시판 수정 요청
        BoardResponseDTO updatedBoardResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(updatedBoardRequestDto)
                .pathParam("id", boardId)
                .when()
                .put("/boards/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(BoardResponseDTO.class);

        // 응답으로 받은 BoardResponseDTO에서 name 값 검증
        assertThat(updatedBoardResponse.name()).isEqualTo(updatedBoardRequestDto.name());

        // DB에서 게시판을 다시 조회하여 수정된 이름이 반영되었는지 확인
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
