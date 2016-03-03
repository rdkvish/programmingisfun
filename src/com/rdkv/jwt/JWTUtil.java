package com.rdkv.jwt;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

public class JWTUtil {

	public static Connection getDatabaseConnection() {
		Connection connection = null;
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:user.db");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully");

		return connection;
	}

	public static void createTable(Connection connection) throws SQLException {

		// create table "Table 1" if not exists
		Statement stmt = connection.createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS registeredUser"
				+ "(id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "'userName' STRING, 'password' STRING, "
				+ "'relationshipName' STRING);";
		stmt.executeUpdate(sql);
		stmt.close();
	}

	static Map<String, String> getXMLConfiguration() {
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File("./resource/clientConfig.xml");

		HashMap<String, String> config = new HashMap<String, String>();

		try {

			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			List list = rootNode.getChildren("property");

			for (int i = 0; i < list.size(); i++) {

				Element node = (Element) list.get(i);
				config.put("id", node.getChildText("id"));
				config.put("key", node.getChildText("key"));
				config.put("skey", node.getChildText("skey"));

			}

		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
		return config;
	}

	@SuppressWarnings("unused")
	private String encode(String message) {
		byte[] bytesToEncode = message.getBytes(StandardCharsets.UTF_8);

		Base64.Encoder encoder = Base64.getUrlEncoder();
		byte[] encoded = encoder.encode(bytesToEncode);
		System.out.println("Encoded: " + new String(encoded));
		return new String(encoded);
	}

	@SuppressWarnings("unused")
	private void decode(byte[] encoded) {
		Base64.Decoder decoder = Base64.getUrlDecoder();
		byte[] decoded = decoder.decode(encoded);
		System.out.println("Decoded: " + new String(decoded));
	}

	public static String generateJWTToken(String userId) {

		HashMap<String, String> config = (HashMap<String, String>) getXMLConfiguration();

		JwtClaims claims = new JwtClaims();
		claims.setExpirationTimeMinutesInTheFuture(8);
		claims.setSubject(userId);
		claims.setIssuer(config.get("id"));
		claims.setAudience(config.get("key"));

		String secret = config.get("skey");
		Key key = null;
		try {
			key = new HmacKey(secret.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JsonWebSignature jws = new JsonWebSignature();
		jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
		jws.setPayload(claims.toJson());
		jws.setKey(key);
		jws.setHeader("kid", "new");
		jws.setHeader("typ", "JWT");
		jws.setDoKeyValidation(false); // relaxes the key length requirement

		String jwt = null;
		try {
			jwt = jws.getCompactSerialization();
		} catch (JoseException e) {
			e.printStackTrace();
		}
		System.out.println("JWT Token: " + jwt);

		return jwt;
	}

	public static void verifyJWTClaim(String jwtString) {
		HashMap<String, String> config = (HashMap<String, String>) getXMLConfiguration();

		String secret = config.get("skey");
		Key key = null;
		try {
			key = new HmacKey(secret.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		JwtConsumer jwtConsumer = new JwtConsumerBuilder()
				.setRequireExpirationTime().setAllowedClockSkewInSeconds(30)
				.setRequireSubject().setExpectedIssuer(config.get("id"))
				.setExpectedAudience(config.get("key")).setVerificationKey(key)
				.setRelaxVerificationKeyValidation().build();

		JwtClaims processedClaims = null;
		try {
			processedClaims = jwtConsumer.processToClaims(jwtString);
		} catch (InvalidJwtException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(processedClaims);
	}
	
}
