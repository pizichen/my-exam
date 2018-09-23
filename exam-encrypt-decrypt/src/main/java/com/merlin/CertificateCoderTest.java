package com.merlin;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Test;

/**
 *　　　　　　　 ┏┓　 ┏┓+ +
 *　　　　　　　┏┛┻━━━┛┻┓ + +
 *　　　　　　　┃　　　　　　┃ 　
 *　　　　　　　┃　　　━　　 ┃ ++ + + +
 *　　　　　　 ████━████  ┃+
 *　　　　　　　┃　　　　　　　┃ +
 *　　　　　　　┃　　　┻　　　┃
 *　　　　　　　┃　　　　　　┃ + +
 *　　　　　　　┗━┓　　　┏━┛
 *　　　　　　　　 ┃　　　┃　　　　　　　　　　　
 *　　　　　　　　 ┃　　　┃ + + + +
 *　　　　　　　　 ┃　　　┃　　　　Code is far away from bug with the animal protecting　　　　　　　
 *　　　　　　　　 ┃　　　┃ + 　　　　神兽保佑,代码无bug　　
 *　　　　　　　　 ┃　　　┃
 *　　　　　　　　 ┃　　　┃　　+　　　　　　　　　
 *　　　　　　　　 ┃　 　 ┗━━━┓ + +
 *　　　　　　　　 ┃ 　　　　   ┣┓
 *　　　　　　　　 ┃ 　　　　　 ┏┛
 *　　　　　　　　 ┗┓┓┏━┳┓┏┛ + + + +
 *　　　　　　　　  ┃┫┫ ┃┫┫
 *　　　　　　　　  ┗┻┛ ┗┻┛+ + + +
 */
/**
 * @Title:  CertificateCoderTest.java   
 * @Package com.merlin   
 * @Description:    证书组件测试类
 * @author: Merlin.Chen   
 * @date:   2018年9月13日 下午1:52:31   
 * @version V1.0
 */
public class CertificateCoderTest {

	private String PASSWORD = "123456";
	
	private String ALIAS = "www.merlin.org";
	
	private String CERTIFICATE_PATH = "/Users/apple/Documents/Java/secret/merlin.cer";
	
	private String KEYSTORE_PATH = "/Users/apple/Documents/Java/secret/merlin.keystore";
	
//	private String CERTIFICATE_PATH = "D:/secret/certs/ca.cer";
//	
//	private String KEYSTORE_PATH = "D:/secret/certs/ca.key";
	
	/**
	 * @Title: testPublicKEnPrivateKDe   
	 * @Description: 公钥加密~私钥解密 
	 * @throws InvalidKeyException
	 * @throws CertificateException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws IOException
	 * @throws UnrecoverableKeyException
	 * @throws KeyStoreException      
	 * @return: void
	 */
	@Test
	public void testPublicKEnPrivateKDe() 
			throws InvalidKeyException, CertificateException, NoSuchAlgorithmException, NoSuchPaddingException, 
			IllegalBlockSizeException, BadPaddingException, IOException, UnrecoverableKeyException, KeyStoreException{
		System.err.println("...公钥加密~私钥解密...");
		String inputStr = "数字证书";
		// 公钥加密
		byte[] encrypt = CertificateCoder.encryptByPublicKey(inputStr.getBytes(), CERTIFICATE_PATH);
		// 私钥解密
		byte[] decrypt = CertificateCoder.decryptByPrivateKey(encrypt, KEYSTORE_PATH, ALIAS, PASSWORD);
		String outputStr = new String(decrypt);
		System.err.println("加密前：\n" + inputStr);
		System.err.println("加密后：\n" + outputStr);
		Assert.assertArrayEquals(inputStr.getBytes(), decrypt);
	}
	
	/**
	 * @Title: testPrivateKEnPublicKDe   
	 * @Description: 私钥加密~公钥解密        
	 * @return: void
	 * @throws IOException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws NoSuchPaddingException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyStoreException 
	 * @throws InvalidKeyException 
	 * @throws UnrecoverableKeyException 
	 */
	@Test
	public void testPrivateKEnPublicKDe() throws UnrecoverableKeyException, InvalidKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException{
		System.err.println("...私钥加密~公钥解密...");
		String inputStr = "数字签名";
		// 私钥加密
		byte[] encodedData = 
				CertificateCoder.encryptByPrivateKey(inputStr.getBytes(), KEYSTORE_PATH, ALIAS, PASSWORD);
		// 公钥解密
		byte[] decodedData = CertificateCoder.decryptByPublicKey(encodedData, CERTIFICATE_PATH);
		String outputStr = new String(decodedData);
		System.err.println("加密前：\n" + inputStr);
		System.err.println("加密后：\n" + outputStr);
		Assert.assertArrayEquals(inputStr.getBytes(), decodedData);
	}
	
	/**
	 * @Title: TestSign   
	 * @Description: 签名验证     
	 * @return: void
	 * @throws IOException 
	 * @throws SignatureException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyStoreException 
	 * @throws InvalidKeyException 
	 * @throws UnrecoverableKeyException 
	 */
	@Test
	public void TestSign() throws 
		UnrecoverableKeyException, InvalidKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, 
		SignatureException, IOException{
		String inputStr = "签名";
		System.err.println("...私钥签名~公钥验证...");
		byte[] sign = CertificateCoder.sign(inputStr.getBytes(), KEYSTORE_PATH, ALIAS, PASSWORD);
		System.err.println("签名：\n" + Hex.encodeHexString(sign));
		// 验证签名
		boolean status = CertificateCoder.verify(inputStr.getBytes(), sign, CERTIFICATE_PATH);
		System.err.println("状态：\n" + status);
		Assert.assertTrue(status);
	}
}
