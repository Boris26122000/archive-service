package io.archiveservice.service.detector;

import static org.apache.commons.lang3.StringUtils.isBlank;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ClientIpDetector {

	private static final String UNKNOWN = "unknown";

	public static Optional<String> getClientIp(HttpServletRequest request) {
		String ip = request.getHeader(HttpHeader.X_FORWARDED_FOR.getKey());
		if (isIpNotFound(ip)) {
			ip = request.getHeader(HttpHeader.PROXY_CLIENT_IP.getKey());
		}
		if (isIpNotFound(ip)) {
			ip = request.getHeader(HttpHeader.WL_PROXY_CLIENT_IP.getKey());
		}
		if (isIpNotFound(ip)) {
			ip = request.getHeader(HttpHeader.HTTP_CLIENT_IP.getKey());
		}
		if (isIpNotFound(ip)) {
			ip = request.getHeader(HttpHeader.HTTP_X_FORWARDED_FOR.getKey());
		}
		if (isIpNotFound(ip)) {
			ip = request.getRemoteAddr();
		}
		return isIpNotFound(ip)
				? Optional.empty()
				: Optional.of(ip);
	}

	private static boolean isIpNotFound(String ip) {
		return isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip);
	}

	@Getter
	@RequiredArgsConstructor
	private enum HttpHeader {
		X_FORWARDED_FOR("X-Forwarded-For"),
		PROXY_CLIENT_IP("Proxy-Client-IP"),
		WL_PROXY_CLIENT_IP("WL-Proxy-Client-IP"),
		HTTP_CLIENT_IP("HTTP_CLIENT_IP"),
		HTTP_X_FORWARDED_FOR("HTTP_X_FORWARDED_FOR");

		private final String key;
	}
}
