package com.merlin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class CRC32Test {

	// 文件路径
	static String path = "/Users/apple/Documents/FileZilla_3.14.1_macosx-x86.app.tar.bz2";
	
	public static void testByMessageDigests() throws NoSuchAlgorithmException, IOException{
		// 构建文件输入流
		FileInputStream fis = new FileInputStream(new File(path));
		// 初始化数字签名对象
		MessageDigest md = MessageDigest.getInstance("MD5");
		// 初始化DigestInputStream
		DigestInputStream dis = new DigestInputStream(fis, md);
		// 设置缓冲区大小
		int buf = 1024;
		// 缓冲区字节数组
		byte[] buffer = new byte[buf];
		// 当读到值大于-1时就继续读
		int read = dis.read(buffer, 0, buf);
		while(read > -1){
			read = dis.read(buffer, 0, buf);
		}
		// 关闭流
		dis.close();
		// 获得MessageDigest
		MessageDigest md5 = dis.getMessageDigest();
		// 摘要处理
		byte[] b = md5.digest();
		// 十六进制转换
		String md5Hex = Hex.encodeHexString(b);
		System.out.println("testByMessageDigests -- md5Hex:"+md5Hex);
		// 63bc7309806c1708e5f4d96a7280fe0f
		
	}
	
	public static void testByDigiestUtils() throws IOException{
		// 构建文件输入流
		FileInputStream fis = new FileInputStream(new File(path));
		String md5Hex = DigestUtils.md5Hex(fis);
		fis.close();
		
		System.out.println("testByDigiestUtils -- md5Hex:"+md5Hex);
		// 63bc7309806c1708e5f4d96a7280fe0f
	}
	
	/**
	 * 终端输入一下命令，将会得到和上述方法一样的结果：63bc7309806c1708e5f4d96a7280fe0f
	 * openssl md5 /Users/apple/Documents/FileZilla_3.14.1_macosx-x86.app.tar.bz2
	 */
	
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		testByMessageDigests();
		testByDigiestUtils();
	}

}
