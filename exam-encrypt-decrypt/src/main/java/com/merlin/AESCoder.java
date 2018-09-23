package com.merlin;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * AES对称加密实现类
 * @author apple
 *
 */
public class AESCoder {
	
	// 密钥算法
	public static final String KEY_ALGORITHM = "AES";
	
	/*
	 *  加解密算法/工作模式/填充方式
	 *  JDK7支持PKCS5Padding填充方式
	 *  Bouncy Castle支持PKCS7Padding填充方式
	 */
	public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	
	/**
	 * 转换密钥
	 * @param key 二级制密钥
	 * @return Key 密钥
	 */
	private static Key toKey(byte[] key){
		SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
		return secretKey;
	}
	
	/**
	 * 解密
	 * @param data 待解密数据
	 * @param key 密钥
	 * @return byte[] 解密数据
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws NoSuchProviderException 
	 */
	public static byte[] decrypt(byte[] data, byte[] key) throws NoSuchAlgorithmException, 
		NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException{
		// 还原密钥
		Key k = toKey(key);
		/*
		 * 实例化
		 * 使用PKCS7Padding填充方式，按如下方式实现
		 * Cipher.getInstance(CIPHER_ALGORITHM, "BC");
		 */
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		// 初始化，设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, k);
		// 执行操作
		return cipher.doFinal(data);
	}
	
	/**
	 * 加密
	 * @param data 待加密数据
	 * @param key 密钥
	 * @return byte[] 加密数据
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws NoSuchProviderException 
	 */
	public static byte[] encrypt(byte[] data, byte[] key) throws NoSuchAlgorithmException, 
		NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException{
		// 还原密钥
		Key k = toKey(key);
		
		/*
		 * 实例化
		 * 使用PKCS7Padding填充方式，按如下方式实现
		 * Cipher.getInstance(CIPHER_ALGORITHM, "BC");
		 */
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM,"BC");
		// 初始化，设置为加密模式
		cipher.init(Cipher.ENCRYPT_MODE, k);
		// 执行操作
		return cipher.doFinal(data);
	}
	
	/**
	 * 生成密钥
	 * @return byte[] 二进制密钥
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException 
	 */
	public static byte[] initKey() throws NoSuchAlgorithmException, NoSuchProviderException{
		Security.addProvider(new BouncyCastleProvider());
		// 实例化
		KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM,"BC");
		// AES要求密钥长度为128、192或256
		kg.init(256);
		// 生成秘密钥匙
		SecretKey secretKey = kg.generateKey();
		// 获得秘密钥匙的二进制编码形式
		return secretKey.getEncoded();
	}
}
