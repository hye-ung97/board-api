package org.board.board.dto.board;

import org.board.board.entity.Board;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 목록 응답 DTO")
public record BoardListResponse(
    @Schema(description = "게시글 ID", example = "1") Long id,
    @Schema(description = "게시글 제목", example = "첫 번째 게시글") String title,
    @Schema(description = "게시글 내용", example = "이것은 첫 번째 게시글의 내용입니다.") String content,
    @Schema(description = "작성자 ID", example = "1") Long memberId) {
  public static BoardListResponse from(Board board) {
    return new BoardListResponse(
        board.getId(), board.getTitle(), board.getContent(), board.getMemberId());
  }
}
