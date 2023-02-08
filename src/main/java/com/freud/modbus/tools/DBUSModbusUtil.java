package com.freud.modbus.tools;

import java.util.Arrays;

public class DBUSModbusUtil {

	public static int bytesToInt(byte[] bytes) {
		int number = bytes[0] & 0xFF;
		// "|="按位或赋值。
		number |= ((bytes[1] << 8) & 0xFF00);
		number |= ((bytes[2] << 16) & 0xFF0000);
		number |= ((bytes[3] << 24) & 0xFF000000);
		return number;
	}

	public static byte[] intToBytes(int value) {
		byte[] byte_src = new byte[4];
		byte_src[3] = (byte) ((value & 0xFF000000) >> 24);
		byte_src[2] = (byte) ((value & 0x00FF0000) >> 16);
		byte_src[1] = (byte) ((value & 0x0000FF00) >> 8);
		byte_src[0] = (byte) ((value & 0x000000FF));
		return byte_src;
	}

	public static byte[] shortToBytes(Short value) {
		byte[] byte_src = new byte[2];
		byte_src[1] = (byte) ((value & 0xFF00) >> 8);
		byte_src[0] = (byte) ((value & 0x00FF));
		return byte_src;
	}

	public static String bytesToHexString(byte[] src) {
		if (src == null || src.length <= 0) {
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder("");
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}

		return stringBuilder.toString();
	}

	public static String intsToHexString(int[] src) {
		if (src == null || src.length <= 0) {
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder("");
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}

		return stringBuilder.toString();
	}

	public static int[] hexStrToIntArray(String str) {
		int strLen = str.length();
		int len = strLen % 2 == 0 ? (strLen / 2) : (strLen / 2 + 1);
		int[] intArray = new int[len];
		for (int i = 0; i < len; i++) {
			if (i < len - 1) {
				intArray[i] = Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
			} else {
				intArray[i] = Integer.parseInt(str.substring(i * 2, strLen), 16);
			}
		}
		return intArray;
	}

	public static byte[] hexStrToBytes(String hexStr) {
		if (hexStr.length() % 2 != 0) {
			throw new IllegalArgumentException("十六进制的字符串长度必须是2的倍数!");
		}
		byte[] ret = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length(); i = i + 2) {
			ret[i / 2] = (byte) Integer.parseInt(hexStr.substring(i, i + 2), 16);
		}
		return ret;
	}

	public static byte[] hexStrToByteArray(String str) {
		int strLen = str.length();
		int len = strLen % 2 == 0 ? (strLen / 2) : (strLen / 2 + 1);
		byte[] intArray = new byte[len];
		try {
			for (int i = 0; i < len; i++) {
				if (i < len - 1) {
					intArray[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
				} else {
					intArray[i] = (byte) Integer.parseInt(str.substring(i * 2, strLen), 16);
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return intArray;
	}

	public static int[] bytesToInts(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		int[] data = new int[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			data[i] = bytes[i] & 0xFF;
		}
		return data;
	}

	public static byte[] intsToBytes(int[] ints) {
		if (ints == null || ints.length == 0) {
			return null;
		}
		byte[] data = new byte[ints.length];
		for (int i = 0; i < ints.length; i++) {
			data[i] = (byte) ints[i];
		}
		return data;
	}

	public static int[] crc(int[] data) {
		int[] temdata = new int[data.length + 2];
		// unsigned char alen = *aStr – 2; //CRC16只计算前两部分
		int xda, xdapoly;
		int i, j, xdabit;
		xda = 0xFFFF;
		xdapoly = 0xA001; // (X**16 + X**15 + X**2 + 1)
		for (i = 0; i < data.length; i++) {
			xda ^= data[i];
			for (j = 0; j < 8; j++) {
				xdabit = (int) (xda & 0x01);
				xda >>= 1;
				if (xdabit == 1)
					xda ^= xdapoly;
			}
		}
		System.arraycopy(data, 0, temdata, 0, data.length);
		temdata[temdata.length - 2] = (int) (xda & 0xFF);
		temdata[temdata.length - 1] = (int) (xda >> 8);
		return temdata;
	}

	public static int[] crc(byte[] data) {
		int[] temdata = new int[data.length + 2];
		// unsigned char alen = *aStr – 2; //CRC16只计算前两部分
		int xda, xdapoly;
		int i, j, xdabit;
		xda = 0xFFFF;
		xdapoly = 0xA001; // (X**16 + X**15 + X**2 + 1)
		for (i = 0; i < data.length; i++) {
			xda ^= data[i];
			for (j = 0; j < 8; j++) {
				xdabit = (int) (xda & 0x01);
				xda >>= 1;
				if (xdabit == 1)
					xda ^= xdapoly;
			}
		}
		System.arraycopy(data, 0, temdata, 0, data.length);
		temdata[temdata.length - 2] = xda & 0xFF;
		temdata[temdata.length - 1] = xda >> 8;
		return temdata;
	}

	static short[] crc16_tab_h = { 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
			0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
			0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
			0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
			0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
			0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
			0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1,
			0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40,
			0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
			0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
			0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
			0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
			0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
			0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
			0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40 };

	static short[] crc16_tab_l = { 0x00, 0xC0, 0xC1, 0x01, 0xC3, 0x03, 0x02, 0xC2, 0xC6, 0x06, 0x07, 0xC7, 0x05, 0xC5,
			0xC4, 0x04, 0xCC, 0x0C, 0x0D, 0xCD, 0x0F, 0xCF, 0xCE, 0x0E, 0x0A, 0xCA, 0xCB, 0x0B, 0xC9, 0x09, 0x08, 0xC8,
			0xD8, 0x18, 0x19, 0xD9, 0x1B, 0xDB, 0xDA, 0x1A, 0x1E, 0xDE, 0xDF, 0x1F, 0xDD, 0x1D, 0x1C, 0xDC, 0x14, 0xD4,
			0xD5, 0x15, 0xD7, 0x17, 0x16, 0xD6, 0xD2, 0x12, 0x13, 0xD3, 0x11, 0xD1, 0xD0, 0x10, 0xF0, 0x30, 0x31, 0xF1,
			0x33, 0xF3, 0xF2, 0x32, 0x36, 0xF6, 0xF7, 0x37, 0xF5, 0x35, 0x34, 0xF4, 0x3C, 0xFC, 0xFD, 0x3D, 0xFF, 0x3F,
			0x3E, 0xFE, 0xFA, 0x3A, 0x3B, 0xFB, 0x39, 0xF9, 0xF8, 0x38, 0x28, 0xE8, 0xE9, 0x29, 0xEB, 0x2B, 0x2A, 0xEA,
			0xEE, 0x2E, 0x2F, 0xEF, 0x2D, 0xED, 0xEC, 0x2C, 0xE4, 0x24, 0x25, 0xE5, 0x27, 0xE7, 0xE6, 0x26, 0x22, 0xE2,
			0xE3, 0x23, 0xE1, 0x21, 0x20, 0xE0, 0xA0, 0x60, 0x61, 0xA1, 0x63, 0xA3, 0xA2, 0x62, 0x66, 0xA6, 0xA7, 0x67,
			0xA5, 0x65, 0x64, 0xA4, 0x6C, 0xAC, 0xAD, 0x6D, 0xAF, 0x6F, 0x6E, 0xAE, 0xAA, 0x6A, 0x6B, 0xAB, 0x69, 0xA9,
			0xA8, 0x68, 0x78, 0xB8, 0xB9, 0x79, 0xBB, 0x7B, 0x7A, 0xBA, 0xBE, 0x7E, 0x7F, 0xBF, 0x7D, 0xBD, 0xBC, 0x7C,
			0xB4, 0x74, 0x75, 0xB5, 0x77, 0xB7, 0xB6, 0x76, 0x72, 0xB2, 0xB3, 0x73, 0xB1, 0x71, 0x70, 0xB0, 0x50, 0x90,
			0x91, 0x51, 0x93, 0x53, 0x52, 0x92, 0x96, 0x56, 0x57, 0x97, 0x55, 0x95, 0x94, 0x54, 0x9C, 0x5C, 0x5D, 0x9D,
			0x5F, 0x9F, 0x9E, 0x5E, 0x5A, 0x9A, 0x9B, 0x5B, 0x99, 0x59, 0x58, 0x98, 0x88, 0x48, 0x49, 0x89, 0x4B, 0x8B,
			0x8A, 0x4A, 0x4E, 0x8E, 0x8F, 0x4F, 0x8D, 0x4D, 0x4C, 0x8C, 0x44, 0x84, 0x85, 0x45, 0x87, 0x47, 0x46, 0x86,
			0x82, 0x42, 0x43, 0x83, 0x41, 0x81, 0x80, 0x40 };

	public static int calcCrc16(byte[] data) {
		return calcCrc16(data, 0, data.length);
	}

	public static int calcCrc16(byte[] data, int offset, int len) {
		return calcCrc16(data, offset, len, 0xffff);
	}

	public static int calcCrc16(byte[] data, int offset, int len, int preval) {
		int ucCRCHi = (preval & 0xff00) >> 8;
		int ucCRCLo = preval & 0x00ff;
		int iIndex;
		for (int i = 0; i < len; ++i) {
			iIndex = (ucCRCLo ^ data[offset + i]) & 0x00ff;
			ucCRCLo = ucCRCHi ^ crc16_tab_h[iIndex];
			ucCRCHi = crc16_tab_l[iIndex];
		}
		return ucCRCHi | ucCRCLo << 8;
	}

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
		return str + lrc_hex.toUpperCase();
	}

	public static byte[] lrc(byte[] bytes) {
		byte chksum = 0;
		for (byte b : bytes) {
			chksum += b;
		}
		byte[] result = Arrays.copyOf(bytes, bytes.length + 1);
		result[bytes.length] = chksum;
		return result;
	}

	public static String convertHexToString(String hex) {

		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < hex.length() - 1; i += 2) {
			String output = hex.substring(i, (i + 2));
			int decimal = Integer.parseInt(output, 16);
			sb.append((char) decimal);
			temp.append(decimal);
		}
		return sb.toString();
	}

	public static String convertIntToHexString(int i) {
		String str = Integer.toHexString(i);
		if (str.length() % 2 == 1) {
			str = "0" + str;
		}
		return str;
	}

	public static String asciiToHex(String str) {
		StringBuilder sb = new StringBuilder();
		byte[] bs = str.getBytes();
		for (int i = 0; i < bs.length; i++)
			sb.append(Integer.toHexString(bs[i]));
		return sb.toString();
	}

	public static String hexToAscii(String hex) {

		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < hex.length() - 1; i += 2) {
			String output = hex.substring(i, (i + 2));
			int decimal = Integer.parseInt(output, 16);
			sb.append((char) decimal);
			temp.append(decimal);
		}
		return sb.toString();
	}

	public static String binaryToHex(String bString) {

		if (bString == null || bString.equals(""))
			throw new IllegalArgumentException("param is null or blank");

		int value = Integer.parseInt(bString, 2);

		String str = Integer.toHexString(value);

		if (str.length() / 2 != 0) {
			str = "0" + str;
		}

		return str;
	}

	public static String hexString2binary(String hexString) {
		if (hexString == null || hexString.length() % 2 != 0)
			return null;
		String bString = "", tmp;
		for (int i = 0; i < hexString.length(); i++) {
			tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
			bString += tmp.substring(tmp.length() - 4);
		}
		return bString;
	}
}
