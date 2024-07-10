package com.kmaengggong.kmaengggong.member.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
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

    public void update(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
