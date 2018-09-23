package com.merlin;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * CipherOutputStream、CipherInputStream对使用DES算法对文件内容加解密
 * 
 * @author aspire
 *
 */
public class CipherOutInputStreamDemo {

	static KeyGenerator kg = null;
	static Cipher cipher = null;
	static SecretKey secretKey = null;
	static {
		try {
			kg = KeyGenerator.getInstance("DES");
			cipher = Cipher.getInstance("DES");
			secretKey = kg.generateKey();
			System.out
					.println(Base64.encodeBase64String(secretKey.getEncoded()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 加密内容并输出到文件
	 * 
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IOException
	 */
	public static void write() throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, IOException {

		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		String input = "数据加解密123";
		CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(
				new File("secret")), cipher);
		DataOutputStream dos = new DataOutputStream(cos);
		dos.writeUTF(input);
		dos.flush();
		dos.close();
	}

	/**
	 * 用全局密钥解密加密后的文件内容，此方法需和write()方法同时执行
	 * 
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IOException
	 */
	public static void read() throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, IOException {

		cipher.init(Cipher.DECRYPT_MODE, secretKey);

		CipherInputStream cis = new CipherInputStream(new FileInputStream(
				new File("secret")), cipher);
		DataInputStream dis = new DataInputStream(cis);
		String output = dis.readUTF();
		dis.close();
		System.out.println("output:" + output);
	}

	/**
	 * 拿到执行加密write()方法时生成的密钥encode字符串反解出密钥，再通过反解的密钥解密加密文件
	 * 
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IOException
	 */
	public static void read(String secretKeyStr) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, IOException {

		byte[] bKey = Base64.decodeBase64(secretKeyStr);

		SecretKey secretKey = new SecretKeySpec(bKey, "DES");

		cipher.init(Cipher.DECRYPT_MODE, secretKey);

		CipherInputStream cis = new CipherInputStream(new FileInputStream(
				new File("secret")), cipher);
		DataInputStream dis = new DataInputStream(cis);
		String output = dis.readUTF();
		dis.close();
		System.out.println("output:" + output);
	}

	public static void main(String[] args) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, IOException {
		// write();
		// read();
		read("4+ybBPdzmLk=");
	}

}
