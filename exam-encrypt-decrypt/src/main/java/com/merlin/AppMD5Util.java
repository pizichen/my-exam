package com.merlin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class AppMD5Util {
	
	 /**
	  * 认证技术引擎类MessageDigests实现MD5加密 1
     * 对字符串md5加密(小写+字母)
     *
     * @param str 传入要加密的字符串
     * @return  MD5加密后的字符串
     */
	/*
		这里有个大坑：（转载自http://webcache.googleusercontent.com/search?q=cache:VmYqeTCPMSwJ:darren.ink/archives/79+&cd=12&hl=zh-CN&ct=clnk）
		PS:	在使用过程中发现还是会有问题，因为是转BigInteger然后输出16进制的字符串，所以导致一定情况下前导的0会丢失，如用上面的代码MD5加密字母a，会发现和其他地方的MD5加密，前面缺少0。
			最后还是建议自己手动将byte数组转16进制的字符串的方法：
			byte[] hash = md.digest(); 
			StringBuilder secpwd = new StringBuilder(); 
			for (int i = 0; i < hash.length; i++) { 
				int v = hash[i] & 0xFF; if (v < 16) secpwd.append(0); 
				secpwd.append(Integer.toString(v, 16)); 
			} 
			return secpwd.toString();
	 */
    public static String md5_ms_hash(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
//            System.out.println(Base64.encodeBase64String(md.digest()));
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
//          return new BigInteger(1, md.digest()).toString(16);
            byte[] hash = md.digest(); 
			StringBuilder secpwd = new StringBuilder(); 
			for (int i = 0; i < hash.length; i++) { 
				int v = hash[i] & 0xFF; if (v < 16) secpwd.append(0); 
				secpwd.append(Integer.toString(v, 16)); 
			} 
			return secpwd.toString();
        } catch (Exception e) {
           e.printStackTrace();
           return null;
        }
    }
    
    
    /**
     * 认证技术引擎类MessageDigests实现MD5加密
     * 对字符串md5加密(大写+数字)
     *
     * @param s 传入要加密的字符串
     * @return  MD5加密后的字符串
     */
    
    public static String md5_md_16(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
 
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            System.out.println("length+algorithn="+mdInst.getDigestLength()+"...."+mdInst.getAlgorithm()+"...."+mdInst.getProvider());
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 认证技术引擎类DigestInputStream实现MD5加密 
     * @param s
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String md5_dis_hash(String s) throws NoSuchAlgorithmException, IOException{
    	byte[] input = s.getBytes();
    	MessageDigest md = MessageDigest.getInstance("MD5");
    	DigestInputStream dis = new DigestInputStream(new ByteArrayInputStream(input), md);
    	dis.read(input, 0, input.length);
    	byte[] output = dis.getMessageDigest().digest();
    	output.clone();
    	StringBuilder secpwd = new StringBuilder(); 
		for (int i = 0; i < output.length; i++) { 
			int v = output[i] & 0xFF; if (v < 16) secpwd.append(0); 
			secpwd.append(Integer.toString(v, 16)); 
		} 
		return secpwd.toString();
    }
    
    
    public static String md5_dos_hash(String s) throws NoSuchAlgorithmException, IOException{
    	byte[] input = s.getBytes();
    	MessageDigest md = MessageDigest.getInstance("MD5");
    	DigestOutputStream dos = new DigestOutputStream(new ByteArrayOutputStream(), md);
    	dos.write(input, 0, input.length);
    	byte[] output = dos.getMessageDigest().digest();
    	dos.flush();
    	dos.close();
    	StringBuilder secpwd = new StringBuilder(); 
		for (int i = 0; i < output.length; i++) { 
			int v = output[i] & 0xFF; if (v < 16) secpwd.append(0); 
			secpwd.append(Integer.toString(v, 16)); 
		} 
		return secpwd.toString();
    }
    
    public static String des(String s) throws NoSuchAlgorithmException, IOException{
    	
    	AlgorithmParameters ap = AlgorithmParameters.getInstance("DES");
    	ap.init(new BigInteger(s).toByteArray());
    	byte[] b = ap.getEncoded();
    	System.out.println("des:"+new BigInteger(b).toString());
    	return null;
    }
    
 
     public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
    	 String content = "去尼玛的";
    	 String md51 = md5_ms_hash(content);
         String md52 = md5_md_16(content);
         String md53 = md5_dis_hash(content);
         String md54 = md5_dos_hash(content);
         System.out.println("md51:"+md51);
         System.out.println("md52:"+md52);
         System.out.println("md53:"+md53);
         System.out.println("md54:"+md54);
         
         String desS = "19050619766489163472469";
         des(desS);
    }

}
