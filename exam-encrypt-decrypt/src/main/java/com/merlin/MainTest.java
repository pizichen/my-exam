package com.merlin;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;

public class MainTest {

	public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException, NoSuchPaddingException {

		MessageDigest md1 = MessageDigest.getInstance("MD5");
		MessageDigest md2 = MessageDigest.getInstance("SHA");
		
		Cipher cp1 = Cipher.getInstance("RSA");
		
		Signature sign1 = Signature.getInstance("MD5WITHRSA");
		
		Mac mac1 = Mac.getInstance("SSLMACMD5");
		
		KeyStore ks1 = KeyStore.getInstance("PKCS12");
		
		/* 认证技术引擎类：
		 * Signature MessageDigest Cipher Mac KeyStore
		 */
		System.out.println("........Signature.......");
		/*
		 * NONEWITHDSA; SHA384WITHECDSA; SHA224WITHDSA; SHA256WITHRSA; MD5WITHRSA; SHA1WITHRSA; 
		 * SHA512WITHRSA; MD2WITHRSA; SHA256WITHDSA; SHA1WITHECDSA; MD5ANDSHA1WITHRSA; SHA224WITHRSA; 
		 * NONEWITHECDSA; NONEWITHRSA; SHA256WITHECDSA; SHA224WITHECDSA; SHA384WITHRSA; SHA512WITHECDSA; SHA1WITHDSA; 
		 */
		Set<String> set1 =  Security.getAlgorithms("Signature");
		set1.forEach((s) -> System.out.print(s + "; "));  
		System.out.println();
		
		System.out.println("........MessageDigest.......");
		// Java8支持的消息摘要算法
		/*
		 * SHA-384; SHA-224; SHA-256; MD2; SHA; SHA-512; MD5; 
		 */
		Set<String> set2 =  Security.getAlgorithms("MessageDigest");
		set2.forEach((s) -> System.out.print(s + "; "));  
		System.out.println();
		
		System.out.println(".......Cipher........");
		/*
		 * PBEWITHHMACSHA384ANDAES_128; AES_256/GCM/NOPADDING; AES_192/GCM/NOPADDING; PBEWITHHMACSHA512ANDAES_128; 
		 * AES_256/CBC/NOPADDING; AES_256/ECB/NOPADDING; PBEWITHHMACSHA224ANDAES_256; AES_128/CBC/NOPADDING; AESWRAP_192; AESWRAP; 
		 * PBEWITHMD5ANDDES; AES_192/CBC/NOPADDING; PBEWITHHMACSHA256ANDAES_256; PBEWITHHMACSHA1ANDAES_128; PBEWITHSHA1ANDRC4_128; 
		 * AES_192/OFB/NOPADDING; AES_128/ECB/NOPADDING; DESEDEWRAP; AESWRAP_256; RC2; PBEWITHSHA1ANDRC4_40; RSA; AESWRAP_128; 
		 * PBEWITHHMACSHA512ANDAES_256; AES_192/CFB/NOPADDING; DESEDE; AES_128/CFB/NOPADDING; AES_192/ECB/NOPADDING; BLOWFISH; 
		 * ARCFOUR; AES_256/CFB/NOPADDING; AES; RSA/ECB/PKCS1PADDING; AES_128/OFB/NOPADDING; AES_128/GCM/NOPADDING; DES; 
		 * PBEWITHHMACSHA256ANDAES_128; PBEWITHSHA1ANDDESEDE; PBEWITHSHA1ANDRC2_40; PBEWITHHMACSHA384ANDAES_256; AES_256/OFB/NOPADDING; 
		 * PBEWITHSHA1ANDRC2_128; PBEWITHMD5ANDTRIPLEDES; PBEWITHHMACSHA1ANDAES_256; PBEWITHHMACSHA224ANDAES_128; 
		 */
		Set<String> set3 =  Security.getAlgorithms("Cipher");
		set3.forEach((s) -> System.out.print(s + "; "));  
		System.out.println();
		
		System.out.println(".......Mac........");
		// 消息摘要算法
		/*
		 * PBEWITHHMACSHA512; PBEWITHHMACSHA224; PBEWITHHMACSHA256; HMACSHA384; PBEWITHHMACSHA384; HMACSHA256; 
		 * HMACPBESHA1; HMACSHA224; HMACMD5; PBEWITHHMACSHA1; SSLMACSHA1; HMACSHA512; SSLMACMD5; HMACSHA1; 
		 */
		Set<String> set4 =  Security.getAlgorithms("Mac");
		set4.forEach((s) -> System.out.print(s + "; "));  
		System.out.println();
		
		System.out.println(".......KeyStore........");
		/*
		 * JKS; JCEKS; PKCS12; CASEEXACTJKS; DKS; WINDOWS-ROOT; WINDOWS-MY; 
		 */
		Set<String> set5 =  Security.getAlgorithms("KeyStore");
		set5.forEach((s) -> System.out.print(s + "; "));  
		System.out.println();
		
		System.out.println(".......Provider........");
		Provider[] providers = Security.getProviders();
		Arrays.asList(providers).forEach((p) -> System.out.println(p + "; "));
		
		for(Provider provider: providers){
			System.out.println(provider);
			provider.entrySet().forEach((e) -> System.out.println("    "+e.getKey()+"::::"+e.getValue()));
		}
		
	}
}
