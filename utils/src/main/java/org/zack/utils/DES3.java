package org.zack.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @Package: com.banck.bis.Tools
 * @ClassName: DES3
 * @Description: 3DES加解密类
 * @date: 2019年8月1日 下午9:01:05
 * 
 * @author dongls
 * @version V2.0
 */
public class DES3 {

	private DES3() {
	}
	
	private static final Log log = LogFactory.getLog(DES3.class);

	// 定义 加密算法,可用 DES,DESede,Blowfish
	private static final String Algorithm = "DESede";

	/**
	 * @Description: 3DES加密方法
	 * @param keybyte
	 *            加密密钥，长度为24字节
	 * @param src
	 *            被加密的数据缓冲区（源）
	 * @return 加密后字节数组
	 */
	public static byte[] encryptMode(byte[] keybyte, byte[] src) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 加密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
		} catch (javax.crypto.NoSuchPaddingException e) {
			log.error(e.getMessage(), e);
		} catch (java.lang.Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * @Description: 3DES解密方法
	 * @param keybyte
	 *            为加密密钥，长度为24字节
	 * @param src
	 *            为加密后的缓冲区
	 * @return 解密后字节数组
	 */
	public static byte[] decryptMode(byte[] keybyte, byte[] src) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 解密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
		} catch (javax.crypto.NoSuchPaddingException e) {
			log.error(e.getMessage(), e);
		} catch (java.lang.Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public static void main(String[] args) {


		final byte[] keyBytes = "123456789012345678901234".getBytes();

		String szSrc = "This is a 3DES test. 测试";
		System.out.println("加密前的字符串:" + szSrc);

		byte[] encoded = encryptMode(keyBytes, szSrc.getBytes());
		System.out.println("加密后的字符串:" + new String(encoded));

		byte[] srcBytes = decryptMode(keyBytes, encoded);
		System.out.println("解密后的字符串:" + (new String(srcBytes)));
	}
}