package com.merlin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * @Title:  CertificateCoder.java   
 * @Package com.merlin   
 * @Description:    证书组件
 * @author: Merlin.Chen   
 * @date:   2018年9月13日 上午10:28:53   
 * @version V1.0
 */
public class CertificateCoder {
	/**
	 * 类型证书X.509
	 */
	public static final String CERT_TYPE = "X.509";
	
	/**
	 * @Title: getPrivateKeyByKeySotre   
	 * @Description: 由KeyStore获取私钥
	 * @param: @param keyStorePath	密钥库路径
	 * @param: @param alias	别名
	 * @param: @param password	密钥库密码
	 * @param: @throws KeyStoreException
	 * @param: @throws UnrecoverableKeyException
	 * @param: @throws NoSuchAlgorithmException
	 * @param: @throws CertificateException
	 * @param: @throws IOException      
	 * @return: PrivateKey	私钥
	 * @throws
	 */
	private static PrivateKey getPrivateKeyByKeySotre(String keyStorePath, String alias, String password) 
			throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, IOException{
		
		KeyStore ks = getKeyStore(keyStorePath, password);
		PrivateKey pk = (PrivateKey)ks.getKey(alias, password.toCharArray());
		return pk;
	}
	
	
	/**
	 * @Title: getKeyStore   
	 * @Description: 获取KeyStore  
	 * @param: @param keyStorePath 密钥库路径
	 * @param: @param password	密钥库密码
	 * @param: @throws KeyStoreException
	 * @param: @throws NoSuchAlgorithmException
	 * @param: @throws CertificateException
	 * @param: @throws IOException      
	 * @return: KeyStore  密钥库    
	 * @throws
	 */
	private static KeyStore getKeyStore(String keyStorePath, String password) 
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException{
		// 实例化密钥库
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//		KeyStore ks = KeyStore.getInstance("BKS");
		// 获得密钥库文件流
		FileInputStream fis = new FileInputStream(keyStorePath);
		// 加载密钥库
		ks.load(fis, password.toCharArray());
		fis.close();
		return ks;
	}
	
	/**
	 * @Title: getPublicKeyByCertificate   
	 * @Description: 获得公钥  
	 * @param certificatePath 证书路径
	 * @return: PublicKey	公钥
	 * @throws IOException 
	 * @throws CertificateException 
	 */
	private static PublicKey getPublicKeyByCertificate(String certificatePath) 
			throws CertificateException, IOException{
		// 获取证书
		Certificate certificate = getCertificate(certificatePath);
		// 获得公钥
		PublicKey publicKey = certificate.getPublicKey();
		return publicKey;
	}
	
	/**
	 * @Title: getCertificate   
	 * @Description: 通过证书路径获取证书
	 * @param certificatePath	证书路径
	 * @throws CertificateException
	 * @throws IOException      
	 * @return: Certificate	证书
	 */
	private static Certificate getCertificate(String certificatePath) 
			throws CertificateException, IOException{
		// 实例化证书工厂
		CertificateFactory certificateFactory = CertificateFactory.getInstance(CERT_TYPE);
		// 获取证书文件输入流
		FileInputStream fis = new FileInputStream(certificatePath);
		Certificate certificate = certificateFactory.generateCertificate(fis);
		fis.close();
		return certificate;
	}
	
	/**
	 * @Title: getCertificate   
	 * @Description: 通过密钥库等相关信息获取证书   
	 * @param keyStorePath	密钥库路径
	 * @param alias	别名
	 * @param password	密码
	 * @return: Certificate	证书
	 * @throws IOException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyStoreException 
	 */
	private static Certificate getCertificate(String keyStorePath, String alias, String password) 
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException{
		// 获取密钥库
		KeyStore ks = getKeyStore(keyStorePath, password);
		Certificate certificate = ks.getCertificate(alias);
		return certificate;
	}
	
	/**
	 * @Title: encryptByPrivateKey   
	 * @Description: 私钥加密
	 * @param data	待加密数据
	 * @param keyStorePath	密钥库路径
	 * @param alias	别名
	 * @param password	私钥库密码
	 * @return: byte[]	加密后的数据
	 * @throws IOException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyStoreException 
	 * @throws UnrecoverableKeyException 
	 * @throws NoSuchPaddingException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String keyStorePath, String alias, String password) 
			throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, 
			IOException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		// 获取私钥
		PrivateKey priavteKey = getPrivateKeyByKeySotre(keyStorePath, alias, password);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(priavteKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, priavteKey);
		byte[] encryptData = cipher.doFinal(data); 
		return encryptData;
	}
	
	/**
	 * @Title: decryptByPrivateKey   
	 * @Description: 通过私钥解密
	 * @param data	待解密数据
	 * @param keyStorePath	私钥库路径
	 * @param alias	别名
	 * @param password	密码
	 * @throws UnrecoverableKeyException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException      
	 * @return: byte[] 解密后的数据
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String keyStorePath, String alias, String password) 
			throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, 
			IOException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		// 获取私钥
		PrivateKey privateKey = getPrivateKeyByKeySotre(keyStorePath, alias, password);
		//对数据解密
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decryptData = cipher.doFinal(data);
		return decryptData;
	}
	
	/**
	 * @Title: encryptByPublicKey   
	 * @Description: 通过公钥加密
	 * @param data	待加密数据
	 * @param certificatePath	证书路径
	 * @return: byte[]	加密后的数据
	 * @throws IOException 
	 * @throws CertificateException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public static byte[] encryptByPublicKey(byte[] data, String certificatePath) 
			throws CertificateException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, 
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		// 取得公钥
		PublicKey publicKey = getPublicKeyByCertificate(certificatePath);
		//对数据加密
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encryptData = cipher.doFinal(data);
		return encryptData;
	}
	
	/**
	 * @Title: decryptByPublicKey   
	 * @Description: 通过公钥解密  
	 * @param data	待解密数据
	 * @param certificatePath	证书路径
	 * @return: byte[]	解密后的数据
	 * @throws IOException 
	 * @throws CertificateException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public static byte[] decryptByPublicKey(byte[] data, String certificatePath) 
			throws CertificateException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, 
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		// 取得公钥
		PublicKey publicKey = getPublicKeyByCertificate(certificatePath);
		// 对数据解密
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		byte[] decryptData = cipher.doFinal(data);
		return decryptData;
	}
	
	/**
	 * @Title: sign   
	 * @Description: 签名
	 * @param sign	代签名数据
	 * @param keySotrePath	密钥库
	 * @param alias	别名
	 * @param password	密码
	 * @return: byte[]	签名后数据
	 * @throws IOException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyStoreException 
	 * @throws UnrecoverableKeyException 
	 * @throws InvalidKeyException 
	 * @throws SignatureException 
	 */
	public static byte[] sign(byte[] sign, String keyStorePath, String alias, String password) 
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException, 
			InvalidKeyException, SignatureException{
		// 获得证书
		X509Certificate x509Certificate = (X509Certificate)getCertificate(keyStorePath, alias, password);
		// 构建签名， 由证书指定签名算法
		Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
		// 获取私钥
		PrivateKey privateKey = getPrivateKeyByKeySotre(keyStorePath, alias, password);
		// 初始化签名，用私钥构建
		signature.initSign(privateKey);
		signature.update(sign);
		byte[] signData = signature.sign();
		return signData;
	}
	
	/**
	 * @Title: verify   
	 * @Description: 验证签名   
	 * @param data	数据
	 * @param sign	签名
	 * @param certificatPath	证书路径
	 * @return: boolean	是否验证成功
	 * @throws IOException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws SignatureException 
	 */
	public static boolean verify(byte[] data, byte[] sign, String certificatePath) 
			throws CertificateException, IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException{
		// 获取证书
		X509Certificate x509Certificate = (X509Certificate)getCertificate(certificatePath);
		// 由证书构建签名
		Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
		// 由证书初始化签名，实际上是使用了证书中的公钥
		signature.initVerify(x509Certificate);
		signature.update(data);
		boolean flag = signature.verify(sign);
		
		return flag;
	}
}
