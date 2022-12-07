package com.huemap.backend.common.security;

import java.time.Duration;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.huemap.backend.common.config.JwtConfig;
import com.huemap.backend.common.redis.RedisService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenProvider {
  private final JwtConfig jwtConfig;
  private final RedisService redisService;

  public String createAccessToken(String username) {
    int expirySeconds = jwtConfig.getExpirySeconds();
    return createToken(username, expirySeconds);
  }

  public String createRefreshToken(String username) {
    int expirySeconds = jwtConfig.getExpirySeconds() * 48 * 14;
    String refreshToken = createToken(username, expirySeconds);
    redisService.setValues(username, refreshToken, Duration.ofMillis(expirySeconds));
    return refreshToken;
  }

  private String createToken(String username, int expirySeconds) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expirySeconds);

    return Jwts.builder()
               .setSubject(username)
               .setIssuedAt(new Date())
               .setExpiration(expiryDate)
               .signWith(SignatureAlgorithm.HS512, jwtConfig.getClientSecret())
               .compact();
  }

  private Date getExpiredTime(String token) {
    Claims claims = Jwts.parser()
                      .setSigningKey(jwtConfig.getClientSecret())
                      .parseClaimsJws(token)
                      .getBody();

    return claims.getExpiration();
  }

  public String getUserEmailFromToken(String token) {
    Claims claims = Jwts.parser()
                        .setSigningKey(jwtConfig.getClientSecret())
                        .parseClaimsJws(token)
                        .getBody();

    return claims.getSubject();
  }

  public boolean validateAccessToken(HttpServletRequest request, String token) {
    try {
      Jwts.parser().setSigningKey(jwtConfig.getClientSecret()).parseClaimsJws(token);
      return true;
    } catch (SignatureException ex) {
      request.setAttribute("exception", "Invalid JWT signature");
    } catch (MalformedJwtException ex) {
      request.setAttribute("exception", "Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      request.setAttribute("exception", "Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      request.setAttribute("exception", "Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      request.setAttribute("exception", "JWT claims string is empty.");
    }

    return false;
  }
}
