package com.freud.ms.util;

import java.math.BigDecimal;
import java.nio.ByteBuffer;

public final class DataUtils {

	public static int getBit(byte value, int position) {
		return (value >> (7 - position)) & 0x01;
	}

	public static int unsigned(byte b) {
		return b & 0xFF;
	}

	//
	// ==============Short=======================
	//
	public static short bytesToShort(byte[] ary) {
		if (ary.length < 2) {
			return (short) ((ary[0] & 0xFF));
		} else {
			return (short) (((ary[0] << 8) & 0xFF00) | (ary[1] & 0xFF));
		}
	}

	public static int bytesToUShort(byte[] ary) {
		if (ary.length < 2) {
			return (int) ((ary[0] & 0xFF));
		} else {
			return (int) (((ary[0] << 8) & 0xFF00) | (ary[1] & 0xFF));
		}
	}

	public static short bytesToInverseShort(byte[] ary) {
		if (ary.length < 2) {
			return (short) ((ary[0] & 0xFF));
		} else {
			return (short) (((ary[1] << 8) & 0xFF00) | (ary[0] & 0xFF));
		}
	}

	public static int bytesToInverseUShort(byte[] ary) {
		if (ary.length < 2) {
			return (int) ((ary[0] & 0xFF));
		} else {
			return (int) (((ary[1] << 8) & 0xFF00) | (ary[0] & 0xFF));
		}
	}

	//
	// ==============Int=======================
	//
	public static int bytesToInt(byte[] ary) {
		if (ary.length != 4) {
			throw new IllegalArgumentException("Length of input bytes array must be " + 4);
		}
		return (int) (((ary[2] << 24) & 0xFF000000) | ((ary[3] << 16) & 0xFF0000) | ((ary[0] << 8) & 0xFF00)
				| (ary[1] & 0xFF));
	}

	public static int bytesToInverseInt(byte[] ary) {
		if (ary.length > 4) {
			throw new IllegalArgumentException("Length of input bytes array must be " + 4);
		}
		return (int) (((ary[0] << 24) & 0xFF000000) | ((ary[1] << 16) & 0xFF0000) | ((ary[2] << 8) & 0xFF00)
				| (ary[3] & 0xFF));
	}

	public static long bytesToUInt(byte[] ary) {
		if (ary.length > 4) {
			throw new IllegalArgumentException("Length of input bytes array must be " + 4);
		}
		long value = bytesToInt(ary);
		if (value >= 0) {
			return value;
		} else {
			value = value & 0x7fffffffL;
			value = value + Integer.MAX_VALUE + 1;
			return value;
		}
	}

	public static long bytesToInverseUInt(byte[] ary) {
		if (ary.length > 4) {
			throw new IllegalArgumentException("Length of input bytes array must be " + 4);
		}
		long value = bytesToInverseInt(ary);
		if (value >= 0)
			return value;
		value = value & 0x7fffffffL;
		value = value + Integer.MAX_VALUE + 1;
		return value;
	}

	//
	// ==============Long=======================
	//
	public static long bytesToLong(byte[] ary) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.put(ary, 0, ary.length);
		buffer.flip();// need flip
		return buffer.getLong();
	}

	public static long bytesToInverseLong(byte[] ary) {
		// 01 23 45 67 SRC
		// 67 45 23 01 DEST
		byte[] res = new byte[] { ary[6], ary[7], ary[4], ary[5], ary[2], ary[3], ary[0], ary[1] };
		return bytesToLong(res);
	}

	public static BigDecimal bytesToULong(byte[] ary) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.put(ary, 0, ary.length);
		buffer.flip();// need flip
		long value = buffer.getLong();
		if (value >= 0)
			return new BigDecimal(value);
		long lowValue = value & 0x7fffffffffffffffL;
		return BigDecimal.valueOf(lowValue).add(BigDecimal.valueOf(Long.MAX_VALUE)).add(BigDecimal.valueOf(1));
	}

	//
	// ==============Float=======================
	//
	public static float bytesToFloat(byte[] ary) {
		int value = bytesToInt(ary);
		return Float.intBitsToFloat(value);
	}

	public static float bytesToInverseFloat(byte[] ary) {
		byte[] bytes = new byte[] { ary[2], ary[3], ary[0], ary[1] };
		int value = bytesToInt(bytes);
		return Float.intBitsToFloat(value);
	}

	public static float bytesToInverseFloatBADC(byte[] ary) {
		byte[] bytes = new byte[] { ary[1], ary[0], ary[3], ary[2] };
		int value = bytesToInt(bytes);
		return Float.intBitsToFloat(value);
	}

	//
	// ==============Double=======================
	//
	public static double bytesToDouble(byte[] ary) {
		return Double.longBitsToDouble(bytesToLong(ary));
	}

	public static double bytesToInverseDouble(byte[] ary) {
		return Double.longBitsToDouble(bytesToInverseLong(ary));
	}

	public static byte[] reverseByteArray(byte[] bytes) {
		int length = bytes.length;
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			result[length - 1 - i] = bytes[i];
		}
		return result;
	}

	public static byte[] combine2ByteArrays(byte[]... src) {

		int length = 0;
		for (byte[] bytes : src) {
			length += bytes.length;
		}

		byte[] ret = new byte[length];

		int count = 0;
		for (byte[] bytes : src) {
			System.arraycopy(bytes, 0, ret, count, bytes.length);
			count += bytes.length;
		}

		return ret;
	}

	public static String bytesToHexStringSplitByBlank(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v).toUpperCase();
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
			stringBuilder.append(' ');
		}
		return stringBuilder.toString();
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v).toUpperCase();
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static byte[] hexStrToBytes(String hexStr) {
		if (hexStr.length() % 2 != 0) {
			throw new IllegalArgumentException("The length of a hexadecimal string must be a multiple of 2!");
		}
		byte[] ret = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length(); i = i + 2) {
			ret[i / 2] = (byte) Integer.parseInt(hexStr.substring(i, i + 2), 16);
		}
		return ret;
	}

	public static byte[] shortToBytes(Short value) {
		byte[] bs = new byte[2];
		bs[0] = (byte) ((value & 0xFF00) >> 8);
		bs[1] = (byte) ((value & 0x00FF));
		return bs;
	}

	public static byte[] intToBytes(int value) {
		byte[] bs = new byte[4];
		bs[0] = (byte) ((value & 0xFF000000) >>> 24);
		bs[1] = (byte) ((value & 0x00FF0000) >>> 16);
		bs[2] = (byte) ((value & 0x0000FF00) >>> 8);
		bs[3] = (byte) (value & 0x000000FF);
		return bs;
	}

	public static byte[] longToBytes(long value) {
		byte[] b = new byte[8];
		b[0] = (byte) (value >>> 56);
		b[1] = (byte) (value >>> 48);
		b[2] = (byte) (value >>> 40);
		b[3] = (byte) (value >>> 32);
		b[4] = (byte) (value >>> 24);
		b[5] = (byte) (value >>> 16);
		b[6] = (byte) (value >>> 8);
		b[7] = (byte) (value & 0x000000FF);
		return b;
	}

	public static byte[] floatToBytes(float f) {
		return intToBytes(Float.floatToIntBits(f));
	}

	public static byte[] doubleToBytes(double d) {
		return longToBytes(Double.doubleToLongBits(d));
	}
}
