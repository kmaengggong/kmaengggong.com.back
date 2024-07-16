package com.kmaengggong.kmaengggong.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.test.web.servlet.ResultMatcher;

import com.jayway.jsonpath.JsonPath;

public class CommonTest {
	public static ResultMatcher isUpdatedAtAfterCreatedAt(String createdAtPath, String updatedAtPath) {
		return result -> {
			String jsonResponse = result.getResponse().getContentAsString();
			String createdAtStr = JsonPath.parse(jsonResponse).read(createdAtPath);
			String updatedAtStr = JsonPath.parse(jsonResponse).read(updatedAtPath);

			DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
			LocalDateTime createdAt = LocalDateTime.parse(createdAtStr, formatter);
			LocalDateTime updatedAt = LocalDateTime.parse(updatedAtStr, formatter);

			if(!updatedAt.isAfter(createdAt)){
				throw new AssertionError("updatedAt is not after createdAt");
			}
		};
	}
}
