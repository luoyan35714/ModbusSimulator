package com.freud.ms.util;

public class CRC16 {

	/**
	 * 将16进制字符转换为byte
	 *
	 * @param hex
	 * @return
	 */
	public static byte hexToByte(String hex) {
		int intValue = Integer.parseInt(hex, 16);
		return (byte) intValue;
	}

	/**
	 * 获取高位在前，低位在后的CRC
	 * 
	 * @param bytes
	 * @return
	 */
	public static byte[] getInverseCRC(byte[] bytes) {
		String result = getCRCString(bytes);

		byte[] crc = new byte[2];
//		 高位在前地位在后
		crc[0] = CRC16.hexToByte(result.substring(0, 2));
		crc[1] = CRC16.hexToByte(result.substring(2, 4));
		return crc;
	}

	/**
	 * 获取低位在前，高位在后的CRC
	 * 
	 * @param bytes
	 * @return
	 */
	public static byte[] getCRC(byte[] bytes) {
		String result = getCRCString(bytes);

		byte[] crc = new byte[2];
		// 低位在前，高位在后
		crc[0] = CRC16.hexToByte(result.substring(2, 4));
		crc[1] = CRC16.hexToByte(result.substring(0, 2));
		return crc;
	}

	private static String getCRCString(byte[] bytes) {
		// CRC寄存器全为1
		int CRC = 0x0000ffff;
		// 多项式校验值
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
		// 结果转换为16进制
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
