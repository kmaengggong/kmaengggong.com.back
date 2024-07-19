package com.kmaengggong.kmaengggong.common.interfaces;

import java.net.URI;

import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;

public class CommonController {
	protected static final String FRONT_URL = "http://localhost:3000";

	protected Long generateGuestId(HttpServletRequest request) {
		String remoteAddr = getClientIp(request);
		return (long) remoteAddr.hashCode();
	}

	protected String getClientIp(HttpServletRequest request) {
		String remoteAddr = request.getHeader("X-FOWARDED-FOR");
		if(remoteAddr == null || "".equals(remoteAddr)) remoteAddr = request.getRemoteAddr();
		return remoteAddr;
	}

	protected static URI getUri(String path, Long id) {
		return UriComponentsBuilder
			.fromHttpUrl(FRONT_URL)
			.path(path)
			.buildAndExpand(id)
			.toUri();
	}
}
