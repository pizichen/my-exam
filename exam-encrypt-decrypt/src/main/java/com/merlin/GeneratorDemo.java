package com.merlin;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Base64;

public class GeneratorDemo {

	private static Base64 base64 = new Base64();
	
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {

		// 实例化KeyPairGenerator对象，并制定DH算法
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("DH");
		// 生成KeyPair对象kp1、kp2
		KeyPair kp1 = kpg.generateKeyPair();
		KeyPair kp2 = kpg.generateKeyPair();
		
		// 实例化KeyAgreement对象
		KeyAgreement keyAgree = KeyAgreement.getInstance(kpg.getAlgorithm());
		// 初始化KeyAgreement对象
		keyAgree.init(kp2.getPrivate());
		// 执行计划
		keyAgree.doPhase(kp1.getPublic(), true);
		// 生成SecretKey对象
		SecretKey secretKey = keyAgree.generateSecret("DES");
		
		String secretKeyEncode = base64.encodeToString( secretKey.getEncoded());
		System.out.println(secretKeyEncode);
		
		
	}

}
 