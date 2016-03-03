package com.rdkv.jwt;

import java.nio.charset.Charset;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;

public class CodecUtli {
	
	static Charset UTF_8 = Charset.forName("UTF-8");

	public static String base64Utf8Encoding(String data) {

		String encodedValue = Base64.encodeBase64String(data.getBytes(Charset
				.forName("UTF-8")));

		return encodedValue;
	}

	public static String base64UrlSafeUtf8Encoding(String data)
			throws EncoderException {

		String encodedValue = Base64.encodeBase64URLSafeString(data
				.getBytes(UTF_8));

		return encodedValue;
	}

	public static String calculateHmacSha256Utf8(String message,
			String signingKey) {
		try {

			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(
					signingKey.getBytes(UTF_8), "HmacSHA256");
			sha256_HMAC.init(secret_key);

			String hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message
					.getBytes(UTF_8)));
			return hash;
		} catch (Exception e) {
			return signingKey;
		}

	}

	public static String calculateHmacSha256UrlSafeUtf8(String message,
			String signingKey) {
		try {

			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(
					signingKey.getBytes(UTF_8), "HmacSHA256");
			sha256_HMAC.init(secret_key);

			String hash = Base64.encodeBase64URLSafeString(sha256_HMAC
					.doFinal(message.getBytes(UTF_8)));
			return hash;
		} catch (Exception e) {
			return signingKey;
		}

	}

	public static String generateJWT(String header, String claim,
			String signingKey, boolean urlSafe) {

		if (urlSafe) {
			try {
				header = base64UrlSafeUtf8Encoding(header);
			} catch (EncoderException e) {
				e.printStackTrace();
			}
		} else
			header = base64Utf8Encoding(header);

		StringBuilder jwtString = new StringBuilder(header);
		jwtString.append(".");

		if (urlSafe) {
			try {
				claim = base64UrlSafeUtf8Encoding(claim);
			} catch (EncoderException e) {
				e.printStackTrace();
			}
		} else
			claim = base64Utf8Encoding(claim);

		jwtString.append(claim);

		String jwt = calculateHmacSha256Utf8(jwtString.toString(), signingKey);

		jwtString.append(".");

		return jwtString.append(jwt).toString();

	}

	public static String generateJWTUrlSafe(String header, String claim,
			String signingKey, boolean urlSafe) throws EncoderException {

		if (urlSafe)
			header = base64UrlSafeUtf8Encoding(header);
		else
			header = base64Utf8Encoding(header);

		StringBuilder jwtString = new StringBuilder(header);
		jwtString.append(".");

		if (urlSafe)
			claim = base64UrlSafeUtf8Encoding(claim);
		else
			claim = base64Utf8Encoding(claim);

		jwtString.append(claim);

		String jwt = "";

		if (urlSafe)
			jwt = calculateHmacSha256UrlSafeUtf8(jwtString.toString(),
					signingKey);
		else
			jwt = calculateHmacSha256Utf8(jwtString.toString(), signingKey);

		jwtString.append(".");

		return jwtString.append(jwt).toString();

	}

}
