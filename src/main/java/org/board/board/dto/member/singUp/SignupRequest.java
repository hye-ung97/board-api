package org.board.board.dto.member.singUp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "회원가입 요청")
public class SignupRequest {

  private static final int USERNAME_MAX_LENGTH = 10;
  private static final int PASSWORD_MAX_LENGTH = 15;

  @Schema(
      description = "사용자명 (4-10자, 영소문자+숫자)",
      example = "user123",
      required = true,
      maxLength = USERNAME_MAX_LENGTH)
  private String username;

  @Schema(
      description = "비밀번호 (8-15자, 영대소문자+숫자)",
      example = "Password123",
      required = true,
      maxLength = PASSWORD_MAX_LENGTH)
  private String password;

  @Schema(description = "가입 유형", example = "ADMIN or USER", required = true)
  private String type;

  public SignupRequest(String username, String password, String type) {
    this.username = username;
    this.password = password;
    this.type = type;
  }
}
