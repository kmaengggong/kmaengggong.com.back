package com.kmaengggong.kmaengggong.member.domain;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;

	@Column(nullable = false)
	private String email;

	private String password;

	private String nickname;

	private LocalDateTime registeredAt;

	private String refreshToken;

	private Role role;

	@PrePersist
	protected void onCreate() {
		this.registeredAt = LocalDateTime.now();
		this.role = Role.USER;
	}

	public void update(String nickname) {
		if(nickname != null) this.nickname = nickname;
	}

	public void updatePassword(String password) {
		if(password != null) this.password = password;
	}

	public void updateRefreshToken(String refreshToken) {
		if(refreshToken != null) this.refreshToken = refreshToken;
	}

	public void passwordEncode(BCryptPasswordEncoder bCryptPasswordEncoder){
		this.password = bCryptPasswordEncoder.encode(this.password);
	}
}
