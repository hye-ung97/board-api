package org.board.board.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.board.board.entity.MemberType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private Long expiration;

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(secret.getBytes());
  }

  public String generateToken(Long id, MemberType type) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expiration);

    return Jwts.builder()
        .subject(id.toString())
        .claim("type", type.getValue())
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSigningKey())
        .compact();
  }

  public Long getIdFromToken(String token) {
    Claims claims =
        Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    return Long.parseLong(claims.getSubject());
  }

  public MemberType getTypeFromToken(String token) {
    Claims claims =
        Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    String typeValue = claims.get("type", String.class);
    return MemberType.fromValue(typeValue);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean isTokenExpired(String token) {
    try {
      Claims claims =
          Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
      return claims.getExpiration().before(new Date());
    } catch (Exception e) {
      return true;
    }
  }
}
