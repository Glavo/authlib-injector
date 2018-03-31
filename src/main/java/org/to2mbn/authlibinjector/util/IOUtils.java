package org.to2mbn.authlibinjector.util;

import static java.nio.charset.StandardCharsets.UTF_8;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public final class IOUtils {

	public static byte[] getURL(String url) throws IOException {
		try (InputStream in = new URL(url).openStream()) {
			return asBytes(in);
		}
	}

	public static byte[] postURL(String url, String contentType, byte[] payload) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", contentType);
		conn.setRequestProperty("Content-Length", String.valueOf(payload.length));
		conn.setDoOutput(true);
		try {
			conn.connect();
			try (OutputStream out = conn.getOutputStream()) {
				out.write(payload);
			}
			try (InputStream in = conn.getInputStream()) {
				return asBytes(in);
			}
		} finally {
			conn.disconnect();
		}
	}

	public static byte[] asBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[8192];
		int read;
		while ((read = in.read(buf)) != -1) {
			out.write(buf, 0, read);
		}
		return out.toByteArray();
	}

	public static String asString(byte[] bytes) {
		return new String(bytes, UTF_8);
	}

	public static String removeNewLines(String input) {
		return input.replace("\n", "")
				.replace("\r", "");
	}

	private IOUtils() {}

}
