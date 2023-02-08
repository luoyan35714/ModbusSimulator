package com.freud.modbus.tools;

import java.math.BigDecimal;
import java.nio.ByteBuffer;

public class DBUSDatabusUtil {

	private DBUSDatabusUtil() {
	}

	public static int getBit(byte value, int position) {
		return (value >> (7 - position)) & 1;
	}

	public static int getBitRange(byte value, int index, int offset) {
		return (value >> (index - offset)) & 0xFF;
	}

	public static int getBits(int value, int from, int to) {
		String str = Integer.toBinaryString(value);
		int len = str.length();
		if (len < 32) {
			for (int i = 0; i < 32 - len; i++) {
				str = "0" + str;
			}
		}
		str = str.substring(str.length() - to, str.length() - from);
		return Integer.parseInt(str, 2);
	}

	public static int unsigned(byte b) {
		return b & 0xFF;
	}

	public static byte getByte(int value, int position) {
		return (byte) ((value >> (position * 8)) & 0xFF);
	}

	public static byte makeChecksum(byte[] bytes) {
		return makeChecksum(bytes, bytes.length - 1);
	}

	public static byte makeChecksum(byte[] bytes, int length) {
		int sum = 0;
		for (int i = 0; i < length; i++) {
			sum += bytes[i];
		}
		return (byte) (sum % 256);
	}

	public static int bytesToASCII(byte[] ary, int offset) {
		StringBuffer sb = new StringBuffer();
		for (int i = offset; i < ary.length; i++) {
			char ascii = (char) ary[i];
			sb.append(ascii);
		}
		int value = 0;
		try {
			value = Integer.parseInt(sb.toString().trim());
		} catch (NumberFormatException e) {
		}
		return value;
	}

	public static float bytesToFloat(byte[] ary, int offset) {
		int value = byteArray2Int(ary);
		return Float.intBitsToFloat(value);
	}

	public static float bytesToInverseFloat(byte[] ary, int offset) {
		byte[] bytes = new byte[] { ary[2], ary[3], ary[0], ary[1] };
		int value = bytesToInt(bytes, offset);
		return Float.intBitsToFloat(value);
	}

	public static float bytesToInverseFloatBADC(byte[] ary, int offset) {
		byte[] bytes = new byte[] { ary[1], ary[0], ary[3], ary[2] };
		int value = bytesToInt(bytes, offset);
		return Float.intBitsToFloat(value);
	}

	public static BigDecimal bytesToULong(byte[] ary, int offset) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.put(ary, 0, ary.length);
		buffer.flip();// need flip
		long value = buffer.getLong();
		if (value >= 0)
			return new BigDecimal(value);
		long lowValue = value & 0x7fffffffffffffffL;
		return BigDecimal.valueOf(lowValue).add(BigDecimal.valueOf(Long.MAX_VALUE)).add(BigDecimal.valueOf(1));
	}

	public static double bytesToDouble(byte[] ary, int offset) {
		return Double.longBitsToDouble(bytesToLong(ary, offset));
	}

	public static double bytesToInverseDouble(byte[] ary, int offset) {
		return Double.longBitsToDouble(bytesToInverseLong(ary, offset));
	}

	public static long bytesToLong(byte[] ary, int offset) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.put(ary, 0, ary.length);
		buffer.flip();// need flip
		return buffer.getLong();
	}

	public static void main(String[] args) {

		byte[] bys = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x40, 0x0A };
		byte[] newBys = new byte[] { bys[6], bys[7], bys[4], bys[5], bys[2], bys[3], bys[0], bys[1] };
		System.out.println(bytesToDouble(newBys, 0));
	}

	public static long bytesToInverseLong(byte[] ary, int offset) {
		// 01 23 45 67 SRC
		// 67 45 23 01 DEST
		byte[] res = new byte[] { ary[6], ary[7], ary[4], ary[5], ary[2], ary[3], ary[0], ary[1] };
		return bytesToLong(res, offset);
	}

	public static int bytesToInt(byte[] ary, int offset) {
		return (int) (((ary[offset + 2] << 24) & 0xFF000000) | ((ary[offset + 3] << 16) & 0xFF0000)
				| ((ary[offset] << 8) & 0xFF00) | (ary[offset + 1] & 0xFF));
	}

	public static int bytesToInverseInt(byte[] ary, int offset) {
		return (int) (((ary[offset] << 24) & 0xFF000000) | ((ary[offset + 1] << 16) & 0xFF0000)
				| ((ary[offset + 2] << 8) & 0xFF00) | (ary[offset + 3] & 0xFF));
	}

	public static long bytesToUInt(byte[] ary, int offset) {
		long value = bytesToInt(ary, offset);
		if (value >= 0)
			return value;
		value = value & 0x7fffffffL;
		value = value + Integer.MAX_VALUE + 1;
		return value;
	}

	public static long bytesToInverseUInt(byte[] ary, int offset) {
		long value = bytesToInverseInt(ary, offset);
		if (value >= 0)
			return value;
		value = value & 0x7fffffffL;
		value = value + Integer.MAX_VALUE + 1;
		return value;
	}

	public static short bytesToShort(byte[] ary, int offset) {
		if (ary.length < 2) {
			return (short) ((ary[offset] & 0xFF));
		} else {
			return (short) (((ary[offset] << 8) & 0xFF00) | (ary[offset + 1] & 0xFF));
		}
	}

	public static int bytesToUShort(byte[] ary, int offset) {
		if (ary.length < 2) {
			return (int) ((ary[offset] & 0xFF));
		} else {
			return (int) (((ary[offset] << 8) & 0xFF00) | (ary[offset + 1] & 0xFF));
		}
	}

	public static short bytesToInverseShort(byte[] ary, int offset) {
		if (ary.length < 2) {
			return (short) ((ary[offset] & 0xFF));
		} else {
			return (short) (((ary[offset + 1] << 8) & 0xFF00) | (ary[offset] & 0xFF));
		}
	}

	public static int bytesToInverseUShort(byte[] ary, int offset) {
		if (ary.length < 2) {
			return (int) ((ary[offset] & 0xFF));
		} else {
			return (int) (((ary[offset + 1] << 8) & 0xFF00) | (ary[offset] & 0xFF));
		}
	}

	public static int byteArray2Int(byte[] bytes) {
		int value = 0;
		int count = bytes.length;
		// 因为整型长度为32位，所以此处count不应该超过4。
		if (count > 4) {
			throw new IllegalArgumentException("入参byte数组长度超过合法长度4。");
		}
		for (int i = 0; i < count; i++) {
			int shift = (count - 1 - i) * 8;
			value += (bytes[i] & 0x000000FF) << shift;// 往高位游
		}
		return value;
	}

	public static byte[] intToByteArray(int intValue, int count) {
		byte[] result = new byte[count];
		for (int i = 0; i < count; i++) {
			result[i] = (byte) ((intValue >> 8 * (count - 1 - i)) & 0xFF);
		}
		return result;
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
			stringBuilder.append(' ');
		}
		return stringBuilder.toString();
	}

	public static String bytes2HexString(byte[] src) {
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

	public static byte[] doubleToBytes(double d) {
		return longToBytes(Double.doubleToLongBits(d));
	}

	public static byte[] floatToBytes(float f) {
		return intToBytes(Float.floatToIntBits(f));
	}

	public static byte[] intToBytes(int i) {
		byte[] b = new byte[4];
		b[0] = (byte) (i >>> 24);
		b[1] = (byte) (i >>> 16);
		b[2] = (byte) (i >>> 8);
		b[3] = (byte) i;
		return b;
	}

	public static byte[] longToBytes(long l) {
		byte[] b = new byte[8];
		b[0] = (byte) (l >>> 56);
		b[1] = (byte) (l >>> 48);
		b[2] = (byte) (l >>> 40);
		b[3] = (byte) (l >>> 32);
		b[4] = (byte) (l >>> 24);
		b[5] = (byte) (l >>> 16);
		b[6] = (byte) (l >>> 8);
		b[7] = (byte) (l);
		return b;
	}
}
