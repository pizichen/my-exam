package com.merlin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyPair;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;

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
 * @Title:  PEMCoder.java   
 * @Package com.merlin   
 * @Description:    PE文件提取密钥对
 * @author: Merlin.Chen   
 * @date:   2018年9月13日 下午6:05:10   
 * @version V1.0
 */
public class PEMCoder {
	
	/**
	 * @Title: readKeyPair   
	 * @Description: PEM文件提取密钥对
	 * @param pemFile	PEM文件对象
	 * @param password	PEM文件密码
	 * @return: KeyPair
	 * @throws IOException 
	 */
	public static KeyPair readKeyPair(File pemFile, char[] password) 
			throws IOException{
		// 构建PEMParser
		PEMParser pemParser = new PEMParser(new FileReader(pemFile));
		// 获取PEM密钥对对象
		Object object = pemParser.readObject();
		//关闭PEMParser
		pemParser.close();
		// 构建PEMDecryptorProvider实例
		PEMDecryptorProvider decProvider = new JcePEMDecryptorProviderBuilder().build(password);
		// 构建JacPEMKeyConverter实例
		JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
		// 获得密钥对
		KeyPair kp = null;
		if(object instanceof PEMEncryptedKeyPair){
			kp = converter.getKeyPair(((PEMEncryptedKeyPair)object).decryptKeyPair(decProvider));
		}else{
			kp = converter.getKeyPair((PEMKeyPair)object);
		}
		return kp;
	}
	
	/**
	 * @Title: readKeyPair   
	 * @Description: 获取KeyPair   
	 * @param pemFileName	PEM文件名
	 * @param password	PEM文件密码
	 * @return: KeyPair	密钥对
	 * @throws IOException 
	 */
	public static KeyPair readKeyPair(String pemFileName, char[] password) throws IOException{
		
		return readKeyPair(new File(pemFileName), password);
	}
	
	
}
