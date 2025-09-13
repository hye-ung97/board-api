package org.board.board.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "게시글 생성 응답")
public class BoardCreateResponse {

  @Schema(description = "게시글 ID", example = "1")
  private Long id;

  @Schema(description = "게시글 제목", example = "첫 번째 게시글")
  private String title;

  @Schema(description = "게시글 내용", example = "이것은 첫 번째 게시글의 내용입니다.")
  private String content;

  @Schema(description = "작성자 ID", example = "1")
  private Long memberId;

  public BoardCreateResponse(Long id, String title, String content, Long memberId) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.memberId = memberId;
  }
}
