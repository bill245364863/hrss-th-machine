package com.apex.hrssmachineexecute.utils;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;


public class AESApopUtil {


	private static final String CHARSET_NAME = "UTF-8";
	private static final String AES_NAME = "AES";
	public static final String ALGORITHM = "AES/CBC/PKCS7Padding";
	
	public static String encrypt(String content, String key) {
		byte[] result = null;
		try {
			/*String errorString = "Failed manually overriding key-length permissions.";
			int newMaxKeyLength;
			try {
				if ((newMaxKeyLength = Cipher.getMaxAllowedKeyLength("AES")) < 256) {
					Class c = Class.forName("javax.crypto.CryptoAllPermissionCollection");
					Constructor con = c.getDeclaredConstructor();
					con.setAccessible(true);
					Object allPermissionCollection = con.newInstance();
					Field f = c.getDeclaredField("all_allowed");
					f.setAccessible(true);
					f.setBoolean(allPermissionCollection, true);
					c = Class.forName("javax.crypto.CryptoPermissions");
					con = c.getDeclaredConstructor();
					con.setAccessible(true);
					Object allPermissions = con.newInstance();
					f = c.getDeclaredField("perms");
					f.setAccessible(true);
					((Map) f.get(allPermissions)).put("*", allPermissionCollection);
					c = Class.forName("javax.crypto.JceSecurityManager");
					f = c.getDeclaredField("defaultPolicy");
					f.setAccessible(true);
					Field mf = Field.class.getDeclaredField("modifiers");
					mf.setAccessible(true);
					mf.setInt(f, f.getModifiers() & ~Modifier.FINAL);
					f.set(null, allPermissions);
					newMaxKeyLength = Cipher.getMaxAllowedKeyLength("AES");
				}
			} catch (Exception e) {
				throw new RuntimeException(errorString, e);
			}
			if (newMaxKeyLength < 256)
				throw new RuntimeException(errorString);*/
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET_NAME), AES_NAME);
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(subBytes(key.getBytes(CHARSET_NAME)));
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec);
			result = cipher.doFinal(content.getBytes(CHARSET_NAME));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return Base64.getEncoder().encodeToString(result);
	}

	public static String decrypt(String content, String key) {
		String result = null;
		try {
			/*String errorString = "Failed manually overriding key-length permissions.";
			int newMaxKeyLength;
			try {
				if ((newMaxKeyLength = Cipher.getMaxAllowedKeyLength("AES")) < 256) {
					Class c = Class.forName("javax.crypto.CryptoAllPermissionCollection");
					Constructor con = c.getDeclaredConstructor();
					con.setAccessible(true);
					Object allPermissionCollection = con.newInstance();
					Field f = c.getDeclaredField("all_allowed");
					f.setAccessible(true);
					f.setBoolean(allPermissionCollection, true);
					c = Class.forName("javax.crypto.CryptoPermissions");
					con = c.getDeclaredConstructor();
					con.setAccessible(true);
					Object allPermissions = con.newInstance();
					f = c.getDeclaredField("perms");
					f.setAccessible(true);
					((Map) f.get(allPermissions)).put("*", allPermissionCollection);
					c = Class.forName("javax.crypto.JceSecurityManager");
					f = c.getDeclaredField("defaultPolicy");
					f.setAccessible(true);
					Field mf = Field.class.getDeclaredField("modifiers");
					mf.setAccessible(true);
					mf.setInt(f, f.getModifiers() & ~Modifier.FINAL);
					f.set(null, allPermissions);
					newMaxKeyLength = Cipher.getMaxAllowedKeyLength("AES");
				}
			} catch (Exception e) {
				throw new RuntimeException(errorString, e);
			}
			if (newMaxKeyLength < 256)
				throw new RuntimeException(errorString);*/
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET_NAME), AES_NAME);
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(subBytes(key.getBytes(CHARSET_NAME)));
			cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
			result = new String(cipher.doFinal(Base64.getDecoder().decode(content)), CHARSET_NAME);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static byte[] subBytes(byte[] src) {
		if (src.length < 16) {
			throw new RuntimeException("密钥长度小于16位!");
		}
		byte[] bs = new byte[16];
		for (int i = 0; i < 16; i++) {
			bs[i] = src[i];
		}
		return bs;
	}

}
