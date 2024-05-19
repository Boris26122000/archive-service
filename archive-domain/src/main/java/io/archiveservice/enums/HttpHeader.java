package io.archiveservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HttpHeader {
	X_FORWARDED_FOR("X-Forwarded-For"),
	PROXY_CLIENT_IP("Proxy-Client-IP"),
	WL_PROXY_CLIENT_IP("WL-Proxy-Client-IP"),
	HTTP_CLIENT_IP("HTTP_CLIENT_IP"),
	HTTP_X_FORWARDED_FOR("HTTP_X_FORWARDED_FOR");

	private final String key;
}
