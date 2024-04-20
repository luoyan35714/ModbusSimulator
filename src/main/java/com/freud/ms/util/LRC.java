package com.freud.ms.util;

public final class LRC {

	public static String lrc(String str) {
		int chksum = 0;
		for (int i = 0; i < str.length() / 2; i++) {
			String eachHex = str.substring(2 * i, 2 * i + 2);
			chksum = chksum + Integer.parseInt(eachHex, 16);
		}
		int remainder = chksum & 0xFF;
		remainder ^= 0xFF;
		int sum = remainder + 1;
		sum &= 0xFF;
		String lrc_hex = Integer.toHexString(sum);
		if (lrc_hex.length() == 1) {
			lrc_hex = "0" + lrc_hex;
		}
		return lrc_hex.toUpperCase();
	}

	public static byte lrc(byte[] bytes) {
		int chksum = 0;
		for (byte b : bytes) {
			chksum += b;
		}
		int remainder = chksum & 0xFF;
		remainder ^= 0xFF;
		int sum = remainder + 1;
		sum &= 0xFF;
		return (byte) sum;
	}
}
