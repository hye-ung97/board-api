package org.board.board.dto.member.login;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "로그인 요청")
public class LoginRequest {

  @NotBlank(message = "사용자명은 필수입니다")
  @Schema(description = "사용자명", example = "user123")
  private String username;

  @NotBlank(message = "비밀번호는 필수입니다")
  @Schema(description = "비밀번호", example = "Password123")
  private String password;

  public LoginRequest(String username, String password) {
    this.username = username;
    this.password = password;
  }
}
