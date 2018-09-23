package com.merlin;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64.Decoder;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
/**
 * 
 * @Title:  RSAUtils.java   
 * @Package com.merlin   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: Merlin.Chen   
 * @date:   2018年9月17日 下午6:03:09   
 * @version V1.0
 */

public class RSAUtils {

	private static String RSA = "RSA";

	private static int KEY_LENGTH = 1024;

	public static KeyPair generateRSAKeyPair(int keyLength) {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
			kpg.initialize(keyLength);
			return kpg.genKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 随机生成RSA密钥对(默认密钥长度为1024) keyLength范围一般取 512~2048
	 * 通过keyPair.getPublic().getEncoded(),
	 * keyPair.getPrivate().getEncoded()分别获取公钥、私钥的字节码。
	 * 为了更直观一点，可以通过Base64编码把字节码转为可读的字符串保存起来
	 * Base64Utils.encode(rsaKeyPair.getPublic().getEncoded());
	 * 
	 * @return
	 */
	public static KeyPair generateRSAKeyPair() {
		return generateRSAKeyPair(KEY_LENGTH);
	}

	/**
	 * 用公钥加密 <br>
	 * 每次加密的字节数，不能超过密钥的长度值除以 8 再减去 11，所以采取分段加密的方式规避
	 *
	 * @param data
	 *            需加密数据的byte数据
	 * @param publicKey
	 *            公钥
	 * @return 加密后的byte型数据
	 */
	public static byte[] encryptData(byte[] data, PublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA);
			// 编码前设定编码方式及密钥
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);

			RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
			// 模长
			int keyLen = rsaPublicKey.getModulus().bitLength() / 8;
			int maxEncryptBlock = keyLen - 11;

			// 如果明文长度大于模长-11则要分组加密
			int inputLen = data.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] temp;
			int i = 0;
			// 对数据分段加密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > maxEncryptBlock) {
					temp = cipher.doFinal(data, offSet, maxEncryptBlock);
				} else {
					temp = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(temp, 0, temp.length);
				i++;
				offSet = i * maxEncryptBlock;
			}
			byte[] encryptedData = out.toByteArray();
			out.close();
			
//			return Base64.encodeBase64String(encryptedData);
			// 传入编码数据并返回编码结果
			return encryptedData;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 用私钥解密
	 *
	 * @param encryptedData
	 *            经过encryptedData()加密返回的byte数据
	 * @param privateKey
	 *            私钥
	 * @return
	 */
	public static String decryptData(byte[] encryptedData, PrivateKey privateKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);

			RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
			// 模长
			int keyLen = rsaPrivateKey.getModulus().bitLength() / 8;
			int maxDecryptBlock = keyLen;// 不用减11

			// 如果密文长度大于模长则要分组解密
			int inputLen = encryptedData.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] temp;
			int i = 0;
			// 对数据分段解密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > maxDecryptBlock) {
					temp = cipher.doFinal(encryptedData, offSet,
							maxDecryptBlock);
				} else {
					temp = cipher.doFinal(encryptedData, offSet, inputLen
							- offSet);
				}
				out.write(temp, 0, temp.length);
				i++;
				offSet = i * maxDecryptBlock;
			}
			byte[] decryptedData = out.toByteArray();
			out.close();
			return new String(decryptedData, "utf-8");
//			return decryptedData;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 通过公钥byte[](publicKey.getEncoded())将公钥还原，适用于RSA算法
	 *
	 * @param keyBytes
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PublicKey getPublicKey(byte[] keyBytes)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	/**
	 * 通过私钥byte[]将私钥还原，适用于RSA算法
	 *
	 * @param keyBytes
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PrivateKey getPrivateKey(byte[] keyBytes)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	/**
	 * 使用N、e值还原公钥
	 *
	 * @param modulus
	 * @param publicExponent
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PublicKey getPublicKey(String modulus, String publicExponent)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		BigInteger bigIntModulus = new BigInteger(modulus, 16);
		BigInteger bigIntPrivateExponent = new BigInteger(publicExponent);
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus,
				bigIntPrivateExponent);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	/**
	 * 使用N、d值还原私钥
	 *
	 * @param modulus
	 * @param privateExponent
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PrivateKey getPrivateKey(String modulus,
			String privateExponent) throws NoSuchAlgorithmException,
			InvalidKeySpecException {
		BigInteger bigIntModulus = new BigInteger(modulus,16);
		BigInteger bigIntPrivateExponent = new BigInteger(privateExponent);
		RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(bigIntModulus, bigIntPrivateExponent);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}
	
	public static void main(String[] args) throws Exception {
//		String exponent = "010001";
//		String modulus = "00866954f44bb2b110464efea0d933b74de536b26367b3d6174b633b20fe9a6deb05e30210a2f21a1e239b58e56e22fac024fbdd2a88a64e746114c3ac5b3c80d5d0fc1db8dcfeff4d3a2fb59022932f1a74ad4ce4cc91b64d88d457685f08a4ed18303d98807f1229faf7081581f6eaff5eb91896d59619ea5a505529891cde99";
//		PrivateKey key = getPrivateKey(modulus, exponent);
//		
//		String keyEncode = Base64Utils.encode(key.getEncoded());
//		System.out.println("keyEncode:"+keyEncode);
		
		// 获取钥匙对 并获取公钥、私钥二进制编码 base64转码后的字符串
		KeyPair keyPair = generateRSAKeyPair();
		byte[] privateEncoded = keyPair.getPrivate().getEncoded();
		byte[] publicEncoded = keyPair.getPublic().getEncoded();
		String privateStr = Base64.encodeBase64String(privateEncoded);
		String publicStr = Base64.encodeBase64String(publicEncoded);
		System.out.println(privateStr);
		System.out.println(publicStr);
		
		
//		String privateStr = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKowXxxT9koamH9W6UxiDeax9dgx8QendQ9N7V2VLiHEA9q7/3mbj/Ml3l+JiKJGZRTf4p5tyNDkzsJXINO9Mgt7i3lvM+ZF52bddB/N3XZpLMJ0KFQJP70BWelEnG+ikFbU+w0Q+RAGDfllsVnmBT2hsNDZ7gzqfYjYMNFMrbbXAgMBAAECgYAeA6XicxROgV4/pu5CIFDfTKdU14nwlaHZbx7bkEGwhsc/PkSSB9NdSGJtau4D4HJXOhzXX/k4jgHA2GjbrtQyUzqRqpU4ggZ7MGIVWlvnLKptdM6EHViBZmslIB5OfZ2B9cR3IAp3kL+vdHMo/SEGw5xVCeTlxuLdKtp/Y+4tQQJBAP0bI3SIrUD8007SnsLCEZe47RSpIE0iXwvnVq/Ab3txWEcg1FmNchM7Hy6asOi03Hcd6ImRnk3pOUiVlAqRBtECQQCsIoc2oQ3jq7CvsYvA99UlaBd7q9WanIfu4RtkGXkiHYq65PddgPo9Quwygx1mKvMo0mq5IMT/hxB8hXBp0B0nAkBkvLxr0UpDp1zi7mlJ0rv1yyBxLK8yhA+YSUbzRIAafY5JIQPjcKyKgulyPxdSR0nIDxtSRabgE4c9IGt/mSFBAkAMVuVOezkdqv8deSLFkH6NEestCGJllPcBns1x3YmbcnaxcxxwbYCCx7ta259N5aJMuNjBsKGul6YM5xvp2etdAkANX3405pqy6j5f+p9rmEFxcAl8FxMFGX5RRdrvsrLqyyeyT2FZBxMwi1nrqze21HTAU50YrDY0kcaJR/BRests";
//		String publicStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqMF8cU/ZKGph/VulMYg3msfXYMfEHp3UPTe1dlS4hxAPau/95m4/zJd5fiYiiRmUU3+KebcjQ5M7CVyDTvTILe4t5bzPmRedm3XQfzd12aSzCdChUCT+9AVnpRJxvopBW1PsNEPkQBg35ZbFZ5gU9obDQ2e4M6n2I2DDRTK221wIDAQAB";
		
		// 通过base64转码后的二进制公钥、私钥字符串，获取公钥和私钥
		String content = "密码学是研究编制密码和破译密码的技术科学。研究密码变化的客观规律，应用////于编制密码以保守通信秘密的，称为编码学；应用于破译密码以获取通信情报的，称为破译学，总称密码学。电报最早是由美国的摩尔斯在1844年发明的，故也被叫做摩尔斯电码。";
		PrivateKey privateKey = getPrivateKey(Base64.decodeBase64(privateStr));
		PublicKey publicKey = getPublicKey(Base64.decodeBase64(publicStr));
		System.out.println("format:"+publicKey.getFormat());
		
		// 读取公钥、私钥的模数（系数）modulus 指数exponent
		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		
		RSAPublicKeySpec keySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
		System.out.println("modulus1:" + keySpec.getModulus().toString());
		System.out.println("publicExponent1:" + keySpec.getPublicExponent().toString());
		
		RSAPrivateKeySpec keySpec2 = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
		System.out.println("modulus2:" + keySpec2.getModulus().toString());
		System.out.println("privateExponent2:" + keySpec2.getPrivateExponent().toString());
		
		byte[] enStr = encryptData(content.getBytes("utf-8"), publicKey);
		System.out.println("enStr2:"+Base64.encodeBase64String(enStr));
		String desStr = decryptData(enStr, privateKey);
		System.out.println("desStr:"+desStr);
		
	}

}
