package org.board.board.dto.member.singUp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "회원가입 응답")
public class SignupResponse {

  @Schema(description = "응답 메시지", example = "회원가입이 성공적으로 완료되었습니다.")
  private String message;

  @Schema(description = "사용자명", example = "user123")
  private String username;

  public SignupResponse(String message, String username) {
    this.message = message;
    this.username = username;
  }
}
