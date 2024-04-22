package com.freud.ms.util;

/**
 * CRC16 Util
 * 
 * @author Freud
 *
 */
public class CRC16 {

	/**
	 * convert hex string to byte
	 *
	 * @param hex
	 * @return
	 */
	public static byte hexToByte(String hex) {
		int intValue = Integer.parseInt(hex, 16);
		return (byte) intValue;
	}

	/**
	 * CRC - High bit in front, low bit in back
	 * 
	 * @param bytes
	 * @return
	 */
	public static byte[] getInverseCRC(byte[] bytes) {
		String result = getCRCString(bytes);

		byte[] crc = new byte[2];
		// High bit in front, low bit in back
		crc[0] = CRC16.hexToByte(result.substring(0, 2));
		crc[1] = CRC16.hexToByte(result.substring(2, 4));
		return crc;
	}

	/**
	 * CRC - Low bit in front, high bit in back
	 * 
	 * @param bytes
	 * @return
	 */
	public static byte[] getCRC(byte[] bytes) {
		String result = getCRCString(bytes);

		byte[] crc = new byte[2];
		// Low bit in front, high bit in back
		crc[0] = CRC16.hexToByte(result.substring(2, 4));
		crc[1] = CRC16.hexToByte(result.substring(0, 2));
		return crc;
	}

	/**
	 * CRC algorithm
	 * 
	 * @param bytes
	 * @return
	 */
	private static String getCRCString(byte[] bytes) {
		int CRC = 0x0000ffff;
		int POLYNOMIAL = 0x0000a001;
		int i, j;
		for (i = 0; i < bytes.length; i++) {
			CRC ^= ((int) bytes[i] & 0x000000ff);
			for (j = 0; j < 8; j++) {
				if ((CRC & 0x00000001) != 0) {
					CRC >>= 1;
					CRC ^= POLYNOMIAL;
				} else {
					CRC >>= 1;
				}
			}
		}
		// convert result to hex string
		String result = Integer.toHexString(CRC).toUpperCase();
		if (result.length() < 4) {
			StringBuffer sb = new StringBuffer("0000");
			result = sb.replace(4 - result.length(), 4, result).toString();
		} else if (result.length() > 4) {
			result = result.substring(result.length() - 4);
		}
		return result;
	}
}
