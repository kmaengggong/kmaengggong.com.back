package com.kmaengggong.kmaengggong.board.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
public class Article implements Likeable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long articleId;

	@Column(nullable = false)
	private Long authorId;

	@Column(nullable = false)
	private String title;

	@Column(length = 2048)
	private String content;

	private String headerImage;

	private Long viewCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		viewCount = 0L;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	public void update(String title, String content, String headerImage, Category category) {
		if(title != null) this.title = title;
		if(content != null) this.content = content;
		if(headerImage != null) this.headerImage = headerImage;
		if(category != null) this.category = category;
		this.updatedAt = LocalDateTime.now();
	}

	public void updateViewCount(Long viewCount){
		this.viewCount = viewCount;
	}

	@Override
	public Long getId() {
		return articleId;
	}

	@Override
	public String getType() {
		return "Article";
	}
}
