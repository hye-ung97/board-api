package org.board.board.controller;

import org.board.board.dto.member.singUp.SignupRequest;
import org.board.board.dto.member.singUp.SignupResponse;
import org.board.board.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "인증 API", description = "회원가입 및 로그인 기능을 제공하는 API")
public class AuthController {

  private final MemberService memberService;

  @Autowired
  public AuthController(MemberService memberService) {
    this.memberService = memberService;
  }

  @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "회원가입 성공",
            content = @Content(schema = @Schema(implementation = SignupResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터 또는 중복된 username"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
      })
  @PostMapping("/sign-up")
  public ResponseEntity<SignupResponse> signup(
      @Parameter(description = "회원가입 정보", required = true) @RequestBody SignupRequest request) {
    try {
      SignupResponse response = memberService.signup(request);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
