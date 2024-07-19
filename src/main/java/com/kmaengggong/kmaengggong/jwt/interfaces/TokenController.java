package com.kmaengggong.kmaengggong.jwt.interfaces;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.kmaengggong.kmaengggong.common.interfaces.CommonController;
import com.kmaengggong.kmaengggong.jwt.application.TokenProvider;
import com.kmaengggong.kmaengggong.jwt.application.TokenService;
import com.kmaengggong.kmaengggong.jwt.interfaces.dto.AccessTokenResponse;
import com.kmaengggong.kmaengggong.member.application.AuthService;
import com.kmaengggong.kmaengggong.member.application.MemberService;
import com.kmaengggong.kmaengggong.member.application.dto.MemberFindDTO;
import com.kmaengggong.kmaengggong.member.domain.Member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class TokenController extends CommonController {
	private final TokenProvider tokenProvider;
	private final TokenService tokenService;
	private final MemberService memberService;
	private final AuthService authService;
	
	@PostMapping("/valid")
	public ResponseEntity<AccessTokenResponse> isTokenValid(HttpServletRequest request,
			HttpServletResponse response){
		try {
			// TokenAuthenticationFilter에 의해 이미 accessToken은 체크 됨
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			List<String> authorityList = authentication.getAuthorities().stream()
				.map(authority -> authority.toString())
				.toList();
			
			// 관리자 유저
			if(authorityList.contains("ROLE_ADMIN")) {
//				System.out.println("어드민 유저");
			}
			// 일반 유저
			else if(authorityList.contains("ROLE_USER")) {
//				System.out.println("일반 유저");
			}
			// 만료된 유저
			else if(authorityList.contains("ROLE_EXPIRED")) {
//				System.out.println("만료된 유저");
			}
			// 비로그인 유저
			else {
//				System.out.println("비로그인 유저");
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not signed in");
			}
			
			// 일단 엑세스 토큰은 유효하거나 만료된 상태(유효하지 않은 경우는 비로그인 처리)
			// 리프레쉬 토큰은 무조건 valid 해야함
			Cookie[] cookies = request.getCookies();
			String refreshToken = "";
			
			// 쿠키가 존재하지 않음
			if(cookies == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Refresh token is not exists");
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals("refresh_token")) {
					refreshToken = cookie.getValue();
					break;
				}
			}
			
			// 리프레쉬 토큰이 존재하지 않음
			if(refreshToken.isBlank()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Refresh token is not exists");
			
			// 리프레쉬 토큰 유효하지 않음(만료 포함)
			if(!tokenProvider.validToken(refreshToken)) throw new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Refresh token is invalid");		
						
			Long memberId = tokenProvider.getMemberId(refreshToken);
			MemberFindDTO memberFindDTO = memberService.findById(memberId);
			
			// DB에 저장된 리프레쉬 토큰과 일치하지 않음
			// 이 경우엔 다른 사람이 로그인 했거나, 탈취 당했을 가능성이 있으므로 비로그인 처리
			if(!refreshToken.equals(memberFindDTO.getRefreshToken())) throw new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Refresh token is incorrect");
			
			// 리프레쉬 토큰이 유효한 상태
			Member member = authService.getByCredentials(memberFindDTO.getEmail());
			String accessToken = tokenService.createNewAccessTokenAndRefreshToken(request, response, member);
			AccessTokenResponse accessTokenResponse = new AccessTokenResponse(accessToken);
			System.out.println("토큰이 재발급 됨");
			
			return ResponseEntity.ok(accessTokenResponse);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to verfy token validity");
		}
	}
}
