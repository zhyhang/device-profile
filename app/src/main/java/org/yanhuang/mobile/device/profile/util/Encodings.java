package org.yanhuang.mobile.device.profile.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encodings {
	/**
	 * MD5
	 *
	 * @param str 源字符串
	 * @return MD5串
	 */
	public static String md5(String str) {
		if (str == null) {
			return null;
		}
		StringBuilder buf = new StringBuilder();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] data = md.digest(str.getBytes(StandardCharsets.UTF_8));
			for (int datum : data) {
				int i = datum;
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
		} catch (NoSuchAlgorithmException ignored) {
		}
		return buf.toString();
	}
}
