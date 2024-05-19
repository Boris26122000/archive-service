package io.archiveservice.service.detector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import io.archiveservice.enums.HttpHeader;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ClientIpDetectorTest {

	@Mock
	private HttpServletRequest request;

	@Test
	public void testGetClientIp_XForwardedFor() {
		when(request.getHeader(HttpHeader.X_FORWARDED_FOR.getKey())).thenReturn("192.168.1.1");

		Optional<String> clientIp = ClientIpDetector.getClientIp(request);

		assertEquals("192.168.1.1", clientIp.orElse(null));
	}

	@Test
	public void testGetClientIp_ProxyClientIp() {
		when(request.getHeader(HttpHeader.X_FORWARDED_FOR.getKey())).thenReturn(null);
		when(request.getHeader(HttpHeader.PROXY_CLIENT_IP.getKey())).thenReturn("192.168.1.2");

		Optional<String> clientIp = ClientIpDetector.getClientIp(request);

		assertEquals("192.168.1.2", clientIp.orElse(null));
	}

	@Test
	public void testGetClientIp_WlProxyClientIp() {
		when(request.getHeader(HttpHeader.X_FORWARDED_FOR.getKey())).thenReturn(null);
		when(request.getHeader(HttpHeader.PROXY_CLIENT_IP.getKey())).thenReturn(null);
		when(request.getHeader(HttpHeader.WL_PROXY_CLIENT_IP.getKey())).thenReturn("192.168.1.3");

		Optional<String> clientIp = ClientIpDetector.getClientIp(request);

		assertEquals("192.168.1.3", clientIp.orElse(null));
	}

	@Test
	public void testGetClientIp_HttpClientIp() {
		when(request.getHeader(HttpHeader.X_FORWARDED_FOR.getKey())).thenReturn(null);
		when(request.getHeader(HttpHeader.PROXY_CLIENT_IP.getKey())).thenReturn(null);
		when(request.getHeader(HttpHeader.WL_PROXY_CLIENT_IP.getKey())).thenReturn(null);
		when(request.getHeader(HttpHeader.HTTP_CLIENT_IP.getKey())).thenReturn("192.168.1.4");

		Optional<String> clientIp = ClientIpDetector.getClientIp(request);

		assertEquals("192.168.1.4", clientIp.orElse(null));
	}

	@Test
	public void testGetClientIp_HttpXForwardedFor() {
		when(request.getHeader(HttpHeader.X_FORWARDED_FOR.getKey())).thenReturn(null);
		when(request.getHeader(HttpHeader.PROXY_CLIENT_IP.getKey())).thenReturn(null);
		when(request.getHeader(HttpHeader.WL_PROXY_CLIENT_IP.getKey())).thenReturn(null);
		when(request.getHeader(HttpHeader.HTTP_CLIENT_IP.getKey())).thenReturn(null);
		when(request.getHeader(HttpHeader.HTTP_X_FORWARDED_FOR.getKey())).thenReturn("192.168.1.5");

		Optional<String> clientIp = ClientIpDetector.getClientIp(request);

		assertEquals("192.168.1.5", clientIp.orElse(null));
	}

	@Test
	public void testGetClientIp_RemoteAddr() {
		when(request.getHeader(HttpHeader.X_FORWARDED_FOR.getKey())).thenReturn(null);
		when(request.getHeader(HttpHeader.PROXY_CLIENT_IP.getKey())).thenReturn(null);
		when(request.getHeader(HttpHeader.WL_PROXY_CLIENT_IP.getKey())).thenReturn(null);
		when(request.getHeader(HttpHeader.HTTP_CLIENT_IP.getKey())).thenReturn(null);
		when(request.getHeader(HttpHeader.HTTP_X_FORWARDED_FOR.getKey())).thenReturn(null);
		when(request.getRemoteAddr()).thenReturn("192.168.1.6");

		Optional<String> clientIp = ClientIpDetector.getClientIp(request);

		assertEquals("192.168.1.6", clientIp.orElse(null));
	}

	@Test
	public void testGetClientIp_Unknown() {
		when(request.getHeader(HttpHeader.X_FORWARDED_FOR.getKey())).thenReturn("unknown");
		when(request.getHeader(HttpHeader.PROXY_CLIENT_IP.getKey())).thenReturn("unknown");
		when(request.getHeader(HttpHeader.WL_PROXY_CLIENT_IP.getKey())).thenReturn("unknown");
		when(request.getHeader(HttpHeader.HTTP_CLIENT_IP.getKey())).thenReturn("unknown");
		when(request.getHeader(HttpHeader.HTTP_X_FORWARDED_FOR.getKey())).thenReturn("unknown");
		when(request.getRemoteAddr()).thenReturn("unknown");

		Optional<String> clientIp = ClientIpDetector.getClientIp(request);

		assertFalse(clientIp.isPresent());
	}

	@Test
	public void testGetClientIp_Blank() {
		when(request.getHeader(HttpHeader.X_FORWARDED_FOR.getKey())).thenReturn(" ");
		when(request.getHeader(HttpHeader.PROXY_CLIENT_IP.getKey())).thenReturn(" ");
		when(request.getHeader(HttpHeader.WL_PROXY_CLIENT_IP.getKey())).thenReturn(" ");
		when(request.getHeader(HttpHeader.HTTP_CLIENT_IP.getKey())).thenReturn(" ");
		when(request.getHeader(HttpHeader.HTTP_X_FORWARDED_FOR.getKey())).thenReturn(" ");
		when(request.getRemoteAddr()).thenReturn(" ");

		Optional<String> clientIp = ClientIpDetector.getClientIp(request);

		assertFalse(clientIp.isPresent());
	}
}
