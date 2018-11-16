package util;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptUtil {

	public static final String KEY_ALGORITHM = "RSA";
	/** 貌似默认是RSA/NONE/PKCS1Padding，未验证 */
	public static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";
	public static final String PUBLIC_KEY = "publicKey";
	public static final String PRIVATE_KEY = "privateKey";
	private static final String PWD = "hello123";

	/** RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024 */
	public static final int KEY_SIZE = 2048;

	public static final String PLAIN_TEXT = "MANUTD is the greatest club in the world";

	public static String encrypt(String data, String algorithm) {
		try {
			if ("AES".equals(algorithm)) {
				KeyGenerator kgen = KeyGenerator.getInstance("AES");  
				kgen.init(128, new SecureRandom(PWD.getBytes()));  
				SecretKey secretKey = kgen.generateKey();  
				byte[] enCodeFormat = secretKey.getEncoded();  
				SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");  
				Cipher cipher = Cipher.getInstance("AES");// 创建密码器  
				byte[] byteContent = SocketUtil.hexStr2Bytes(data);  
				cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化  
				byte[] result = cipher.doFinal(byteContent);  
				return SocketUtil.byte2HexStr(result); // 加密  
			} else if ("RSA".equals(algorithm)) {
				Map<String, byte[]> keyMap = generateKeyBytes();
				PublicKey publicKey = restorePublicKey(keyMap.get(PUBLIC_KEY));
			    byte[] encodedText = RSAEncode(publicKey, SocketUtil.hexStr2Bytes(data));
			    return SocketUtil.byte2HexStr(encodedText);
			} else {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
		return data;
	}

	public static String decrypt(String data, String algorithm) {
		try {
			if ("AES".equals(algorithm)) {
				KeyGenerator kgen = KeyGenerator.getInstance("AES");  
				kgen.init(128, new SecureRandom(PWD.getBytes()));  
				SecretKey secretKey = kgen.generateKey();  
				byte[] enCodeFormat = secretKey.getEncoded();  
				SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");              
				Cipher cipher = Cipher.getInstance("AES");// 创建密码器  
				cipher.init(Cipher.DECRYPT_MODE, key);// 初始化  
				byte[] result = cipher.doFinal(SocketUtil.hexStr2Bytes(data));  
				return SocketUtil.byte2HexStr(result); // 加密  
			} else if ("RSA".equals(algorithm)) {
				Map<String, byte[]> keyMap = generateKeyBytes();
				PrivateKey privateKey = restorePrivateKey(keyMap.get(PRIVATE_KEY));
				byte[] srcStr = RSADecode(privateKey, SocketUtil.hexStr2Bytes(data));
				return SocketUtil.byte2HexStr(srcStr);
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
		return data;
	}
	
	public static void main(String[] args) throws Exception{
//	    String hex = "232305FE5755584959555446434330303230303935020029110607161E000101000000006175746F74657374000000000000000000000000313233343536373801EB";
//	    String encryptHex = encrypt(hex, "RSA");
//	    System.out.println("encryptHex:"+encryptHex);
//	    String decriptHex = decrypt(encryptHex, "RSA");
//	    System.out.println("decriptHex:"+decriptHex);
		
		
		Map<String, byte[]> keyMap = generateKeyBytes();
		PublicKey publicKey = restorePublicKey(keyMap.get(PUBLIC_KEY));
//	    byte[] encodedText = RSAEncode(publicKey, SocketUtil.hexStr2Bytes(data));
	    
	    
		
		
	}

	/**
	 * 生成密钥对。注意这里是生成密钥对KeyPair，再由密钥对获取公私钥
	 * 
	 * @return
	 */
	public static Map<String, byte[]> generateKeyBytes() {

		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator
					.getInstance(KEY_ALGORITHM);
			keyPairGenerator.initialize(KEY_SIZE);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

			Map<String, byte[]> keyMap = new HashMap<String, byte[]>();
			keyMap.put(PUBLIC_KEY, publicKey.getEncoded());
			keyMap.put(PRIVATE_KEY, privateKey.getEncoded());
			return keyMap;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 还原公钥，X509EncodedKeySpec 用于构建公钥的规范
	 * 
	 * @param keyBytes
	 * @return
	 */
	public static PublicKey restorePublicKey(byte[] keyBytes) {
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);

		try {
			KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
			PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);
			return publicKey;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 还原私钥，PKCS8EncodedKeySpec 用于构建私钥的规范
	 * 
	 * @param keyBytes
	 * @return
	 */
	public static PrivateKey restorePrivateKey(byte[] keyBytes) {
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
				keyBytes);
		try {
			KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
			PrivateKey privateKey = factory
					.generatePrivate(pkcs8EncodedKeySpec);
			return privateKey;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 加密，三步走。
	 * 
	 * @param key
	 * @param plainText
	 * @return
	 */
	public static byte[] RSAEncode(PublicKey key, byte[] plainText) {

		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(plainText);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 解密，三步走。
	 * 
	 * @param key
	 * @param encodedText
	 * @return
	 */
	public static byte[] RSADecode(PrivateKey key, byte[] encodedText) {

		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(encodedText);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

}
