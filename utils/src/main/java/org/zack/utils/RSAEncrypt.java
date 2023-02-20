package org.zack.utils;

import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @Package: com.banck.bis.Tools
 * @ClassName: RSAEncrypt
 * @Description: RSA 加密解密工具类
 * @date: 2019年8月20日 上午9:39:20
 * 
 * @author dongls
 * @version V2.0
 */
public class RSAEncrypt {
	private RSAEncrypt() {
	}
	
	private static final String publicKey = "publicKey";
	private static final String privateKey = "privateKey";

	private static Map<Class<?>,RSAEncrypt> instanceMap = new HashMap<>();
	private Map<String, String> keyMap; // 用于封装随机产生的公钥与私钥
	
	public static void main(String[] args) throws Exception {
		// 生成公钥和私钥
		
		RSAEncrypt rsaEncrypt = getInstance();
		// 加密字符串
		String message = "df723820";
		System.out.println("随机生成的公钥为:" + rsaEncrypt.keyMap.get(publicKey));
		System.out.println("随机生成的私钥为:" + rsaEncrypt.keyMap.get(privateKey));
		String messageEn = rsaEncrypt.encrypt(message, rsaEncrypt.keyMap.get(publicKey));
		System.out.println(message + "\t加密后的字符串为:" + messageEn);
		String messageDe = rsaEncrypt.decrypt(messageEn.getBytes("UTF-8"), rsaEncrypt.keyMap.get(privateKey));
		System.out.println("还原后的字符串为:" + messageDe);
	}

	/**
	 * @Description: 获取一个独特的RSA加密工具类对象
	 * @param isGenerateNewKey
	 *            是否生成新的密钥对
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static synchronized RSAEncrypt getInstance(Class<?> clazz) throws NoSuchAlgorithmException {

		// 从缓存中获取加密工具类
		RSAEncrypt rsaEncrypt = instanceMap.get(clazz);
		//若没有取到则创建一个
		if (rsaEncrypt == null) {
			rsaEncrypt = new RSAEncrypt();
			rsaEncrypt.genKeyPair();
			instanceMap.put(clazz, rsaEncrypt);
		}

		// 当未生成过密钥时自动生成一个新的密钥对
		if (rsaEncrypt.keyMap == null) {
			rsaEncrypt.genKeyPair();
		}

		return rsaEncrypt;
	}

	/**
	 * @Description: 获取通用RSA加密工具类对象
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static synchronized RSAEncrypt getInstance() throws NoSuchAlgorithmException {
		return getInstance(RSAEncrypt.class);
	}

	/**
	 * 随机生成密钥对
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	private void genKeyPair() throws NoSuchAlgorithmException {
		if (keyMap == null)
			keyMap = new HashMap<String, String>();
		// KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		// 初始化密钥对生成器，密钥大小为96-1024位
		keyPairGen.initialize(1024, new SecureRandom());
		// 生成一个密钥对，保存在keyPair中
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate(); // 得到私钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic(); // 得到公钥
		String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
		// 得到私钥字符串
		String privateKeyString = new String(Base64.encodeBase64(privateKey.getEncoded()));
		// 将公钥和私钥保存到Map
		keyMap.put(RSAEncrypt.publicKey, publicKeyString); 
		keyMap.put(RSAEncrypt.privateKey, privateKeyString); 
	}

	/**
	 * @Description: 获取当前公钥
	 * @return
	 */
	public String getPublicKey() {
		if (keyMap == null)
			return null;
		return keyMap.get(publicKey);
	}

	/**
	 * @Description: 获取当前私钥
	 * @return
	 */
	public String getPrivateKey() {
		if (keyMap == null)
			return null;
		return keyMap.get(privateKey);
	}

	/**
	 * RSA公钥加密
	 * 
	 * @param str
	 *            加密字符串
	 * @param publicKey
	 *            公钥
	 * @return 密文
	 * @throws Exception
	 *             加密过程中的异常信息
	 */
	public String encrypt(String str, String publicKey) throws Exception {
		// base64编码的公钥
		byte[] decoded = Base64.decodeBase64(publicKey);
		RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
				.generatePublic(new X509EncodedKeySpec(decoded));
		// RSA加密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
		return outStr;
	}

	/**
	 * RSA私钥解密
	 * 
	 * @param str
	 *            加密字符串
	 * @param privateKey
	 *            私钥
	 * @return 铭文
	 * @throws Exception
	 *             解密过程中的异常信息
	 */
	public String decrypt(byte[] bytes, String privateKey) throws Exception {
		// 64位解码加密后的字符串
		byte[] inputByte = Base64.decodeBase64(bytes);
		// base64编码的私钥
		byte[] decoded = Base64.decodeBase64(privateKey);
		RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
				.generatePrivate(new PKCS8EncodedKeySpec(decoded));
		// RSA解密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, priKey);
		String outStr = new String(cipher.doFinal(inputByte));
		return outStr;
	}

}