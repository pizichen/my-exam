package com.merlin;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

/**
 * AES加密算法测试类
 * @author apple
 *
 */
public class AESCoderTest {
	
	@Test
	public final void test() throws NoSuchAlgorithmException, InvalidKeyException, 
		NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException{
		String inputStr = "AES";
		byte[] inputData = inputStr.getBytes();
		System.err.println("原文：\t"+inputStr);
		//初始化密钥
		byte[] key = AESCoder.initKey();
//		byte[] key = Base64.decodeBase64("tnjjtxdS7sdMfra2cGqmDn6Xvk3PZ+HTYGT8G3iJAu0=");
		System.err.println("密钥：\t"+Base64.encodeBase64String(key));
		//加密
		inputData = AESCoder.encrypt(inputData, key);
		System.err.println("加密后：\t"+Base64.encodeBase64String(inputData));
		//解密
		byte[] outputData = AESCoder.decrypt(inputData, key);
		String outputStr = new String(outputData);
		System.err.println("解密后：\t"+outputStr);
		Assert.assertEquals(inputStr, outputStr);
	}
}
