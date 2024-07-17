package com.kmaengggong.kmaengggong.common.interfaces;

import jakarta.servlet.http.HttpServletRequest;

public class CommonController {
	protected Long generateGuestId(HttpServletRequest request) {
		String remoteAddr = getClientIp(request);
		return (long) remoteAddr.hashCode();
	}

	protected String getClientIp(HttpServletRequest request) {
		String remoteAddr = request.getHeader("X-FOWARDED-FOR");
		if(remoteAddr == null || "".equals(remoteAddr)) remoteAddr = request.getRemoteAddr();
		return remoteAddr;
	}
}
