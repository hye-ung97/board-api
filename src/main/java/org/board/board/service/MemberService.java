package org.board.board.service;

import java.util.regex.Pattern;

import org.board.board.dto.member.login.LoginRequest;
import org.board.board.dto.member.login.LoginResponse;
import org.board.board.dto.member.signUp.SignupRequest;
import org.board.board.dto.member.signUp.SignupResponse;
import org.board.board.entity.Member;
import org.board.board.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-z0-9]{4,10}$");
  private static final Pattern PASSWORD_PATTERN =
      Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,15}$");

  public MemberService(
      MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.memberRepository = memberRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  public SignupResponse signup(SignupRequest request) {
    if (!USERNAME_PATTERN.matcher(request.getUsername()).matches()) {
      throw new IllegalArgumentException("username은 4-10자의 영소문자와 숫자로만 구성되어야 합니다.");
    }

    if (!PASSWORD_PATTERN.matcher(request.getPassword()).matches()) {
      throw new IllegalArgumentException("password는 8-15자의 영대소문자와 숫자로만 구성되어야 합니다.");
    }

    if (memberRepository.existsByUsername(request.getUsername())) {
      throw new IllegalArgumentException("이미 존재하는 username입니다.");
    }

    String encodedPassword = passwordEncoder.encode(request.getPassword());

    Member user = new Member(request.getUsername(), encodedPassword, request.getType());
    memberRepository.save(user);

    return new SignupResponse("회원가입이 성공적으로 완료되었습니다.", user.getUsername());
  }

  public LoginResponse login(LoginRequest request) {
    String username = request.getUsername();
    String password = request.getPassword();

    Member user =
        memberRepository
            .findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 username입니다."));

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    String token = jwtService.generateToken(user.getId(), user.getType());
    return new LoginResponse("로그인이 성공적으로 완료되었습니다.", token, user.getUsername());
  }
}
