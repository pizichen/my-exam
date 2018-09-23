package com.merlin;

import java.io.IOException;
import java.math.BigInteger;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class DESUtil {
	
	
	public static void algorithmMethed() throws NoSuchAlgorithmException, IOException, NoSuchProviderException{
		
		Security.addProvider(new BouncyCastleProvider());
		AlgorithmParameterGenerator apg = AlgorithmParameterGenerator.getInstance("DES", "BC");
		apg.init(256);
		AlgorithmParameters ap = apg.generateParameters();
		byte[] b = ap.getEncoded();
		System.out.println(new BigInteger(b).toString());
		
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException, NoSuchProviderException {
		
		algorithmMethed();
	}
}
