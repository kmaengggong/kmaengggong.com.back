package com.kmaengggong.kmaengggong.jwt.application;

import org.springframework.stereotype.Service;

import com.kmaengggong.kmaengggong.jwt.utils.CookieUtil;
import com.kmaengggong.kmaengggong.member.domain.Member;
import com.kmaengggong.kmaengggong.member.domain.MemberRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {
	private final TokenProvider tokenProvider;
	private final MemberRepository memberRepository;
	
	public String createNewAccessTokenAndRefreshToken(HttpServletRequest request,
			HttpServletResponse response,
			Member member) {
		// 리프레쉬 토큰 생성 후 저장
		String refreshToken = createNewRefreshToken(member);
		System.out.println("refreshToken 발급: " + refreshToken);

		// 액세스 토큰 생성
		String accessToken = createNewAccessToken(member);
		System.out.println("accessToken 발급: " + accessToken);

		// 리프레쉬 토큰과 액세스 토큰 쿠키에 저장
		int cookieMaxAge = (int) TokenProvider.REFRESH_TOKEN_DURATION.toSeconds();
		CookieUtil.deleteCookie(request, response, CookieUtil.REFRESH_TOKEN_COOKIE_NAME);
		CookieUtil.addCookie(response, CookieUtil.REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
		
		return accessToken;
	}
	
	public String createNewRefreshToken(Member member) {
		String refreshToken = tokenProvider.generateToken(member, TokenProvider.REFRESH_TOKEN_DURATION);
		updateRefreshToken(member, refreshToken);
		return refreshToken;
	}
	
	public void updateRefreshToken(Member member, String refreshToken) {
		member.updateRefreshToken(refreshToken);
		memberRepository.save(member);
	}
	
	public String createNewAccessToken(Member member) {
		return tokenProvider.generateToken(member, TokenProvider.ACCESS_TOKEN_DURATION);
	}
	
	public void deleteRefreshToken(HttpServletRequest request,
			HttpServletResponse response) {
		CookieUtil.deleteCookie(request, response, CookieUtil.REFRESH_TOKEN_COOKIE_NAME);
	}
}
