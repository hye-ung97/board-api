package org.board.board.dto.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "게시글 생성 요청")
public class BoardCreateRequest {

  private static final int TITLE_MAX_LENGTH = 100;
  private static final int CONTENT_MAX_LENGTH = 1000;

  @NotBlank(message = "제목은 필수입니다")
  @Size(max = TITLE_MAX_LENGTH, message = "제목은 " + TITLE_MAX_LENGTH + "자를 초과할 수 없습니다")
  @Schema(description = "게시글 제목", example = "첫 번째 게시글", maxLength = TITLE_MAX_LENGTH)
  private String title;

  @NotBlank(message = "내용은 필수입니다")
  @Size(max = CONTENT_MAX_LENGTH, message = "내용은 " + CONTENT_MAX_LENGTH + "자를 초과할 수 없습니다")
  @Schema(description = "게시글 내용", example = "이것은 첫 번째 게시글의 내용입니다.", maxLength = CONTENT_MAX_LENGTH)
  private String content;

  public BoardCreateRequest(String title, String content) {
    this.title = title;
    this.content = content;
  }
}
