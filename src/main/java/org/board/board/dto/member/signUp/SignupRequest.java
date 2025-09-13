package org.board.board.dto.member.signUp;

import org.board.board.entity.MemberType;

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

  @Schema(description = "가입 유형", example = "USER", required = true)
  private MemberType type;

  public SignupRequest(String username, String password, MemberType type) {
    this.username = username;
    this.password = password;
    this.type = type;
  }
}
