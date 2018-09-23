package com.merlin;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;
import org.junit.Before;
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
 * @Title:  PEMCoderTest.java   
 * @Package com.merlin   
 * @Description:    PEM获取密钥测试类  
 * @author: Merlin.Chen   
 * @date:   2018年9月13日 下午6:29:08   
 * @version V1.0
 */
public class PEMCoderTest {

		private String PASSWORD = "123456";
		// server.key.em client.key.pem 都可以
		private String PEM_FILE_PATH = "D:/secret/private/ca.key.pem";
		
		private PublicKey publicKey;
		
		private PrivateKey privateKey;
		
		private String SIGN_ALGTHEM = "SHA1withRSA";
		
		private byte[] data;
		
		
		
		@Before
		public void initKeyPair() throws IOException{
			data = "PEM".getBytes();
			Security.addProvider(new BouncyCastleProvider());
			KeyPair kp = PEMCoder.readKeyPair(PEM_FILE_PATH, PASSWORD.toCharArray());
			publicKey = kp.getPublic();
			privateKey = kp.getPrivate();
			String pub = Base64.encodeBase64String(publicKey.getEncoded());
			String pri = Base64.encodeBase64String(privateKey.getEncoded());
			System.err.println("public...\n"+pub);
			System.err.println("private...\n"+pri);
		}
		
		@Test
		public void encryptAndDecrypt() 
				throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, 
				BadPaddingException{
			// 公钥加密
			Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] encryptData = cipher.doFinal(data);
			// 私钥加密
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] decryptData = cipher.doFinal(encryptData);
			System.err.println("after data:" + new String(decryptData));
			Assert.assertArrayEquals(data, decryptData);
		}
		
		@Test
		public void sign() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException{
			Signature signature = Signature.getInstance(SIGN_ALGTHEM);
			signature.initSign(privateKey);
			signature.update(data);
			byte[] sign = signature.sign();
			// 用公钥对签名进行校验
			signature.initVerify(publicKey);
			signature.update(data);
			// 验证签名是否正常
			boolean status = signature.verify(sign);
			Assert.assertTrue(status);
		}
}
