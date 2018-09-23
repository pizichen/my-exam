package com.merlin;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

public class SymmetricTest {

	static String CONTENT = "Java加密与解密艺术";
	
	/**
	 * 生成DES秘密钥匙encode字符串,对指定内容进行加密
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public static byte[] createDESSecretKeyEncode() throws NoSuchAlgorithmException, 
		NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, 
		BadPaddingException{
		// 实例化密钥生成器
		KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
		// 初始化长度。 Java7只提供了56位长度， Bouncy Castle提供了64位长度的实现
		keyGenerator.init(56);
		// 生成秘密钥匙
		SecretKey secretKey = keyGenerator.generateKey();
		// 获得秘密钥匙的二级制密码形式
		byte[] b = secretKey.getEncoded();
		// 实例化
		Cipher cipher = Cipher.getInstance("DES");
		// 初始化为加密模式
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		// 执行
		byte[] data = cipher.doFinal(CONTENT.getBytes());
		System.out.println("createDESSecretKeyEncode--data:"+Base64.encodeBase64String(data));
		System.out.println("createDESSecretKeyEncode--secretKeyEncode:"+Base64.encodeBase64String(b));
		return b;
	}
	
	/**
	 * 通过encode字符串还原DES秘密钥匙对象，对指定内容进行解密
	 * @param keyEncode
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public static void backDESSecretKey(String keyEncode) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		byte[] b = Base64.decodeBase64(keyEncode);
		// 实例化DES密钥材料
		DESKeySpec dks = new DESKeySpec(b);
		// 实例化秘密钥匙工厂
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = skf.generateSecret(dks);
		
		// 实例化
		Cipher cipher = Cipher.getInstance("DES");
		// 初始化为加密模式
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		// 执行
		byte[] data = cipher.doFinal(Base64.decodeBase64("rbtKrURNf7tPfHuvdiS+vszfX6JunBoeSjkxQ3PrWQU="));
		
		System.out.println("backDESSecretKey--data:"+new String(data));
		
		System.out.println(Base64.encodeBase64String(secretKey.getEncoded()));
	}
	
	
	public static void main(String[] args) throws InvalidKeyException, 
		NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, 
		IllegalBlockSizeException, BadPaddingException {
//		createDESSecretKeyEncode();
		backDESSecretKey("cymKfNOrhsE=");
		
	}

}
