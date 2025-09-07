package org.board.board.dto.member.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "로그인 응답")
public class LoginResponse {

  @Schema(description = "응답 메시지", example = "로그인이 성공적으로 완료되었습니다.")
  private String message;

  @Schema(description = "JWT 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
  private String token;

  @Schema(description = "사용자명", example = "user123")
  private String username;

  public LoginResponse(String message, String token, String username) {
    this.message = message;
    this.token = token;
    this.username = username;
  }
}
