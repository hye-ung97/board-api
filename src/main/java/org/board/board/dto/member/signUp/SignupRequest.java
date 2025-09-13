package org.board.board.dto.member.signUp;

import org.board.board.entity.MemberType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

  @NotBlank(message = "사용자명은 필수입니다")
  @Size(min = 4, max = USERNAME_MAX_LENGTH, message = "사용자명은 4-10자여야 합니다")
  @Pattern(regexp = "^[a-z0-9]+$", message = "사용자명은 영소문자와 숫자로만 구성되어야 합니다")
  @Schema(
      description = "사용자명 (4-10자, 영소문자+숫자)",
      example = "user123",
      maxLength = USERNAME_MAX_LENGTH)
  private String username;

  @NotBlank(message = "비밀번호는 필수입니다")
  @Size(min = 8, max = PASSWORD_MAX_LENGTH, message = "비밀번호는 8-15자여야 합니다")
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]+$", 
           message = "비밀번호는 영대소문자와 숫자를 포함해야 합니다")
  @Schema(
      description = "비밀번호 (8-15자, 영대소문자+숫자)",
      example = "Password123",
      maxLength = PASSWORD_MAX_LENGTH)
  private String password;

  @NotNull(message = "가입 유형은 필수입니다")
  @Schema(description = "가입 유형", example = "USER")
  private MemberType type;

  public SignupRequest(String username, String password, MemberType type) {
    this.username = username;
    this.password = password;
    this.type = type;
  }
}
