package cafeboard;

import cafeboard.board.BoardRequestDto;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BoardApiTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 게시판목록조회() {
        RestAssured
                .given().log().all()
                .when()
                .get("/boards") // GET /boards 요청
                .then().log().all()
                .statusCode(HttpStatus.OK.value()); // 상태코드가 200인지 검증
    }

    @Test
    void 게시판상세조회() {
        Long boardId = 1L; // 예시로 1번 ID를 사용

        RestAssured
                .given().log().all()
                .pathParam("id", boardId)  // pathParam("id", boardId)
                .when()
                .get("/boards/{id}") // GET /boards/{id} 요청
                .then().log().all()
                .statusCode(HttpStatus.OK.value())  // 200 OK
                .body("id", equalTo(boardId.intValue())); // id 값 검증
    }


    @Test
    void 게시판생성() {
        BoardRequestDto boardRequestDto = new BoardRequestDto("새 게시판");

        RestAssured
                .given().log().all()
                .contentType("application/json")
                .body(boardRequestDto)
                .when()
                .post("/boards") // POST /boards 요청
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value()) // 상태코드가 201인지 검증
                .body("name", equalTo(boardRequestDto.name())) // 응답 본문에서 name이 일치하는지 검증
                .body("createdAt", notNullValue()) // createdAt 필드가 null이 아닌지 검증
                .body("updatedAt", notNullValue()); // updatedAt 필드가 null이 아닌지 검증
    }

    @Test
    void 게시판수정() {
        Long boardId = 1L; // 예시로 1번 ID를 사용
        BoardRequestDto updatedBoardRequestDto = new BoardRequestDto("수정된 게시판");

        // 게시판이 존재하는지 확인 후 진행
        RestAssured
                .given().log().all()
                .contentType("application/json")
                .body(updatedBoardRequestDto)
                .pathParam("id", boardId)
                .when()
                .put("/boards/{id}") // PUT /boards/{id} 요청
                .then().log().all()
                .statusCode(HttpStatus.OK.value()) // 상태코드가 200인지 검증
                .body("name", equalTo(updatedBoardRequestDto.name())); // 응답 본문에서 name이 일치하는지 검증
    }

    @Test
    void 게시판삭제() {
        Long boardId = 1L; // 예시로 1번 ID를 사용

        // 게시판이 존재하는지 확인 후 삭제
        RestAssured
                .given().log().all()
                .pathParam("id", boardId)
                .when()
                .delete("/boards/{id}") // DELETE /boards/{id} 요청
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value()); // 상태코드가 204인지 검증

        // 삭제 후 다시 조회 시 404 Not Found가 반환되는지 확인
        RestAssured
                .given().log().all()
                .pathParam("id", boardId)
                .when()
                .get("/boards/{id}") // GET /boards/{id} 요청
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value()); // 상태코드가 404인지 검증
    }
}
