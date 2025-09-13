package org.board.board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.board.board.dto.member.login.LoginRequest;
import org.board.board.dto.member.login.LoginResponse;
import org.board.board.dto.member.signUp.SignupRequest;
import org.board.board.dto.member.signUp.SignupResponse;
import org.board.board.entity.Member;
import org.board.board.entity.MemberType;
import org.board.board.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

  @Mock private MemberRepository memberRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private JwtService jwtService;

  @InjectMocks private MemberService memberService;

  private Member testMember;
  private SignupRequest validSignupRequest;
  private LoginRequest validLoginRequest;

  @BeforeEach
  void setUp() {
    testMember = new Member("testuser", "encodedPassword", MemberType.USER);
    testMember.setId(1L); // 테스트용 ID 설정
    validSignupRequest = new SignupRequest("testuser", "Password123", MemberType.USER);
    validLoginRequest = new LoginRequest("testuser", "Password123");
  }

  @Nested
  @DisplayName("회원가입 테스트")
  class SignupTest {

    @Test
    @DisplayName("유효한 정보로 회원가입 시 성공")
    void signup_WithValidData_ShouldSucceed() {
      // given
      when(memberRepository.existsByUsername("testuser")).thenReturn(false);
      when(passwordEncoder.encode("Password123")).thenReturn("encodedPassword");
      when(memberRepository.save(any(Member.class))).thenReturn(testMember);

      // when
      SignupResponse response = memberService.signup(validSignupRequest);

      // then
      assertThat(response.getMessage()).isEqualTo("회원가입이 성공적으로 완료되었습니다.");
      assertThat(response.getUsername()).isEqualTo("testuser");
      verify(memberRepository).existsByUsername("testuser");
      verify(passwordEncoder).encode("Password123");
      verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("username이 4자 미만일 때 실패")
    void signup_WithShortUsername_ShouldFail() {
      // given
      SignupRequest request = new SignupRequest("abc", "Password123", MemberType.USER);

      // when & then
      assertThatThrownBy(() -> memberService.signup(request))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("username은 4-10자의 영소문자와 숫자로만 구성되어야 합니다.");
      verify(memberRepository, never()).existsByUsername(anyString());
    }

    @Test
    @DisplayName("username이 10자 초과일 때 실패")
    void signup_WithLongUsername_ShouldFail() {
      // given
      SignupRequest request = new SignupRequest("testuser123", "Password123", MemberType.USER);

      // when & then
      assertThatThrownBy(() -> memberService.signup(request))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("username은 4-10자의 영소문자와 숫자로만 구성되어야 합니다.");
      verify(memberRepository, never()).existsByUsername(anyString());
    }

    @Test
    @DisplayName("username에 대문자가 포함될 때 실패")
    void signup_WithUppercaseInUsername_ShouldFail() {
      // given
      SignupRequest request = new SignupRequest("TestUser", "Password123", MemberType.USER);

      // when & then
      assertThatThrownBy(() -> memberService.signup(request))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("username은 4-10자의 영소문자와 숫자로만 구성되어야 합니다.");
      verify(memberRepository, never()).existsByUsername(anyString());
    }

    @Test
    @DisplayName("username에 특수문자가 포함될 때 실패")
    void signup_WithSpecialCharInUsername_ShouldFail() {
      // given
      SignupRequest request = new SignupRequest("test@user", "Password123", MemberType.USER);

      // when & then
      assertThatThrownBy(() -> memberService.signup(request))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("username은 4-10자의 영소문자와 숫자로만 구성되어야 합니다.");
      verify(memberRepository, never()).existsByUsername(anyString());
    }

    @Test
    @DisplayName("password가 8자 미만일 때 실패")
    void signup_WithShortPassword_ShouldFail() {
      // given
      SignupRequest request = new SignupRequest("testuser", "Pass123", MemberType.USER);

      // when & then
      assertThatThrownBy(() -> memberService.signup(request))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("password는 8-15자의 영대소문자와 숫자로만 구성되어야 합니다.");
      verify(memberRepository, never()).existsByUsername(anyString());
    }

    @Test
    @DisplayName("password가 15자 초과일 때 실패")
    void signup_WithLongPassword_ShouldFail() {
      // given
      SignupRequest request = new SignupRequest("testuser", "Password123456789", MemberType.USER);

      // when & then
      assertThatThrownBy(() -> memberService.signup(request))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("password는 8-15자의 영대소문자와 숫자로만 구성되어야 합니다.");
      verify(memberRepository, never()).existsByUsername(anyString());
    }

    @Test
    @DisplayName("password에 소문자가 없을 때 실패")
    void signup_WithNoLowercaseInPassword_ShouldFail() {
      // given
      SignupRequest request = new SignupRequest("testuser", "PASSWORD123", MemberType.USER);

      // when & then
      assertThatThrownBy(() -> memberService.signup(request))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("password는 8-15자의 영대소문자와 숫자로만 구성되어야 합니다.");
      verify(memberRepository, never()).existsByUsername(anyString());
    }

    @Test
    @DisplayName("password에 대문자가 없을 때 실패")
    void signup_WithNoUppercaseInPassword_ShouldFail() {
      // given
      SignupRequest request = new SignupRequest("testuser", "password123", MemberType.USER);

      // when & then
      assertThatThrownBy(() -> memberService.signup(request))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("password는 8-15자의 영대소문자와 숫자로만 구성되어야 합니다.");
      verify(memberRepository, never()).existsByUsername(anyString());
    }

    @Test
    @DisplayName("password에 숫자가 없을 때 실패")
    void signup_WithNoDigitInPassword_ShouldFail() {
      // given
      SignupRequest request = new SignupRequest("testuser", "Password", MemberType.USER);

      // when & then
      assertThatThrownBy(() -> memberService.signup(request))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("password는 8-15자의 영대소문자와 숫자로만 구성되어야 합니다.");
      verify(memberRepository, never()).existsByUsername(anyString());
    }

    @Test
    @DisplayName("password에 특수문자가 포함될 때 실패")
    void signup_WithSpecialCharInPassword_ShouldFail() {
      // given
      SignupRequest request = new SignupRequest("testuser", "Password123!", MemberType.USER);

      // when & then
      assertThatThrownBy(() -> memberService.signup(request))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("password는 8-15자의 영대소문자와 숫자로만 구성되어야 합니다.");
      verify(memberRepository, never()).existsByUsername(anyString());
    }

    @Test
    @DisplayName("이미 존재하는 username으로 회원가입 시 실패")
    void signup_WithExistingUsername_ShouldFail() {
      // given
      when(memberRepository.existsByUsername("testuser")).thenReturn(true);

      // when & then
      assertThatThrownBy(() -> memberService.signup(validSignupRequest))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("이미 존재하는 username입니다.");
      verify(memberRepository).existsByUsername("testuser");
      verify(passwordEncoder, never()).encode(anyString());
      verify(memberRepository, never()).save(any(Member.class));
    }
  }

  @Nested
  @DisplayName("로그인 테스트")
  class LoginTest {

    @Test
    @DisplayName("유효한 정보로 로그인 시 성공")
    void login_WithValidCredentials_ShouldSucceed() {
      // given
      when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(testMember));
      when(passwordEncoder.matches("Password123", "encodedPassword")).thenReturn(true);
      when(jwtService.generateToken(1L, MemberType.USER)).thenReturn("jwt-token");

      // when
      LoginResponse response = memberService.login(validLoginRequest);

      // then
      assertThat(response.getMessage()).isEqualTo("로그인이 성공적으로 완료되었습니다.");
      assertThat(response.getToken()).isEqualTo("jwt-token");
      assertThat(response.getUsername()).isEqualTo("testuser");
      verify(memberRepository).findByUsername("testuser");
      verify(passwordEncoder).matches("Password123", "encodedPassword");
      verify(jwtService).generateToken(1L, MemberType.USER);
    }

    @Test
    @DisplayName("존재하지 않는 username으로 로그인 시 실패")
    void login_WithNonExistentUsername_ShouldFail() {
      // given
      when(memberRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> memberService.login(new LoginRequest("nonexistent", "Password123")))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("존재하지 않는 username입니다.");
      verify(memberRepository).findByUsername("nonexistent");
      verify(passwordEncoder, never()).matches(anyString(), anyString());
      verify(jwtService, never()).generateToken(any(Long.class), any());
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 시 실패")
    void login_WithWrongPassword_ShouldFail() {
      // given
      when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(testMember));
      when(passwordEncoder.matches("WrongPassword", "encodedPassword")).thenReturn(false);

      // when & then
      assertThatThrownBy(() -> memberService.login(new LoginRequest("testuser", "WrongPassword")))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("비밀번호가 일치하지 않습니다.");
      verify(memberRepository).findByUsername("testuser");
      verify(passwordEncoder).matches("WrongPassword", "encodedPassword");
      verify(jwtService, never()).generateToken(any(Long.class), any());
    }
  }
}
