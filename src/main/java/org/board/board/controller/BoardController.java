package org.board.board.controller;

import jakarta.validation.Valid;

import org.board.board.dto.board.BoardCreateRequest;
import org.board.board.dto.board.BoardCreateResponse;
import org.board.board.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/boards")
@CrossOrigin(origins = "*")
@Tag(name = "게시판 API", description = "게시글 관련 기능을 제공하는 API")
public class BoardController {

  private final BoardService boardService;

  public BoardController(BoardService boardService) {
    this.boardService = boardService;
  }

  @Operation(summary = "게시글 생성", description = "새로운 게시글을 생성합니다.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "게시글 생성 성공",
            content = @Content(schema = @Schema(implementation = BoardCreateResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
      })
  @PostMapping
  public ResponseEntity<BoardCreateResponse> createBoard(
      @Parameter(description = "게시글 생성 정보", required = true) @Valid @RequestBody
          BoardCreateRequest request,
      Authentication authentication) {
    try {
      Long memberId = Long.parseLong(authentication.getName());

      BoardCreateResponse response = boardService.createBoard(request, memberId);

      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
