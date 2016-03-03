package com.rdkv.jwt;

import org.apache.commons.codec.EncoderException;

public class JWTSample {

	public static void main(String[] args) {

		String header = "{\"alg\": \"HS256\",\"typ\": \"JWT\"}";

		String claim = "{\"sub\": \"1234567890\",\"name\": \"John Doe\",\"admin\": true}";

		String key = "secret";

		// key = CodecUtli.base64Utf8Encoding(key);

		try {
			System.out.println(CodecUtli.generateJWTUrlSafe(header, claim, key,
					true));
		} catch (EncoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
