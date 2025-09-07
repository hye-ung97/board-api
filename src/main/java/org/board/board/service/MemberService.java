package org.board.board.service;

import java.util.regex.Pattern;

import org.board.board.dto.member.singUp.SignupRequest;
import org.board.board.dto.member.singUp.SignupResponse;
import org.board.board.entity.Member;
import org.board.board.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-z0-9]{4,10}$");
  private static final Pattern PASSWORD_PATTERN =
      Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,15}$");

  @Autowired
  public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
    this.memberRepository = memberRepository;
    this.passwordEncoder = passwordEncoder;
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
}
