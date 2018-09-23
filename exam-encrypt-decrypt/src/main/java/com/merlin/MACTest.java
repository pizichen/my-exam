package com.merlin;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;


public class MACTest {

	public static void main(String[] args) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {

//		byte[] b = "Java加密解密".getBytes();
		byte[] b = "密".getBytes();
		String str = Base64.encodeBase64String(b);
		byte[] bb = Base64.encodeBase64(b);
		System.out.println(str);
		System.out.println(new String(bb));
		
	/*	String dataS = "Java加密与解密艺术";
//		getSecrety(dataS.getBytes());
//		encodeStr:A7fFinNj67+p7r5nvBMnaw==
//		keyStr:qtUGMIeri9ESdPnCC0gsKyUzxND/1xQrgA+7kdR4X3ImceVlHnaJDnKpu0SjNJB4Ssbf8R0zu43fnjxkDhMFHQ==
//		String keyStr = "qtUGMIeri9ESdPnCC0gsKyUzxND/1xQrgA+7kdR4X3ImceVlHnaJDnKpu0SjNJB4Ssbf8R0zu43fnjxkDhMFHQ==";
		backSecrety(getSecrety(dataS.getBytes()),dataS.getBytes());
	*/
		
		String longHexStr = Long.toHexString(23232323232323L);
		System.out.println(longHexStr);
		
		CRC32 crc32 = new CRC32();
		crc32.update(b);
		System.out.println(crc32.getValue());
		System.out.println(Long.toHexString(crc32.getValue()));
	}
	
	/**
	 * 获取SecretKey字符串
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException 
	 */
	public static String getSecrety(byte[] data) throws NoSuchAlgorithmException, InvalidKeyException{
		// HmacSHA512 赞为提供：HmacRipeMD128、HmacRipeMD160
		KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA512");
		SecretKey secretKey = keyGenerator.generateKey();
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		String encodeStr = Base64.encodeBase64String(mac.doFinal(data));
		String keyStr = Base64.encodeBase64String(secretKey.getEncoded());
		System.out.println("encodeStr1:"+encodeStr);
		System.out.println("keyStr:"+keyStr);
		return keyStr;
	}
	
	/**
	 * 还原SecretKey
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException 
	 */
	public static String backSecrety(String keyStr, byte[] data) throws NoSuchAlgorithmException, 
		InvalidKeyException{
		byte[] secretKeyEncode = Base64.decodeBase64(keyStr);
		// 还原秘密钥匙
		SecretKey secretKey = new SecretKeySpec(secretKeyEncode, "HmacSHA512");
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		String encodeStr2 = Base64.encodeBase64String(mac.doFinal(data));
		System.out.println("encodeStr2:"+encodeStr2);
		return encodeStr2;
	}
	
	// 循环冗余校验算法 -- CRC算法
	/**
	 * 奇偶校验码、 循环冗余校验码、 CRC32 都是一套东西，他们和CRC有着密切的联系，CRC并不属于加密算法范畴。
	 * CRC（Cycile Redundancy Check）是可以根据数据产生简短固定位数的一种散列函数
	 */

}
