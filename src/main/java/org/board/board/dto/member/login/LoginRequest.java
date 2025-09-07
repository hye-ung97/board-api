package org.board.board.dto.member.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "로그인 요청")
public class LoginRequest {

  @Schema(description = "사용자명", example = "user123", required = true)
  private String username;

  @Schema(description = "비밀번호", example = "Password123", required = true)
  private String password;

  public LoginRequest(String username, String password) {
    this.username = username;
    this.password = password;
  }
}
