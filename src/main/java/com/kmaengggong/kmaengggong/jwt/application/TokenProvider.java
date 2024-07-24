package com.kmaengggong.kmaengggong.jwt.application;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.kmaengggong.kmaengggong.member.domain.Member;
import com.kmaengggong.kmaengggong.member.domain.MemberRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenProvider {
	public static final Duration REFRESH_TOKEN_DURATION = Duration.ofHours(2);
	public static final Duration ACCESS_TOKEN_DURATION = Duration.ofMinutes(20);
	
	private final MemberRepository memberRepository;
	
	@Value("${jwt.issuer}")
	private String issuer;
	@Value("${jwt.secret_key}")
	private String secretKey;
	
	public String generateToken(Member member, Duration expiredAt) {
		Date now = new Date();
		return makeToken(member, new Date(now.getTime() + expiredAt.toMillis()));
	}
	
	private String makeToken(Member member, Date expiry) {
		Date now = new Date();
		return Jwts.builder()
				.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
				.setIssuer(issuer)
				.setIssuedAt(now)
				.setExpiration(expiry)
				.setSubject(member.getEmail())
				.claim("memberId",  member.getMemberId())  // 클레임에는 PK 제공
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
	}
	
	public boolean validToken(String token) {
		try {
			Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token);
			return true;
		} catch(ExpiredJwtException e) {  // 만료된 상황만 예외처리
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public Authentication getAuthentication(String token) {
		Set<SimpleGrantedAuthority> authorities = null;
		
		try {
			Claims claims = getClaims(token);
			String role = memberRepository.findByEmail(claims.getSubject())
				.get().getRole().toString();
			
			
			if(role.equals("ROLE_ADMIN")) {
				authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
				System.out.println("TokenProvider: ROLE_ADMIN");
			}
			else {
				authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
				System.out.println("TokenProvider: ROLE_USER");
			}
			
			return new UsernamePasswordAuthenticationToken(
					new org.springframework.security.core.userdetails.User(
							claims.getSubject(), "", authorities), token, authorities
			);
		} catch(ExpiredJwtException e) {  // 한 번 더 만료된 상황만 예외처리
			authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_EXPIRED"));
			System.out.println("TokenProvider: ROLE_EXPIRED");
			
			return new UsernamePasswordAuthenticationToken(
					new org.springframework.security.core.userdetails.User(
							"Unkown", "", authorities), token, authorities
			);
		}
	}
	
	public Long getMemberId(String token) {
		Claims claims = getClaims(token);
		return claims.get("memberId", Long.class);
	}
	
	public String getEmail(String token) {
		Claims claims = getClaims(token);
		return claims.get("sub", String.class);
	}
	
	public Date getExpirationDate(String token) {
		Claims claims = getClaims(token);
		return claims.getExpiration();
	}
	
	public boolean isTokenExpired(String token) {
		return !getExpirationDate(token).before(new Date());
	}
	
	private Claims getClaims(String token) {
		return Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token)
				.getBody();
	}
}
