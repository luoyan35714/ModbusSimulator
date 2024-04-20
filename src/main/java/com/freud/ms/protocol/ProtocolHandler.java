package com.freud.ms.protocol;

import com.freud.ms.config.GlobalConfiguration;
import com.freud.ms.config.models.ModbusDataDefinition;
import com.freud.ms.exception.ModbusExceptionCode;
import com.freud.ms.protocol.data.DataStore;
import com.freud.ms.util.DataUtils;

public abstract class ProtocolHandler {

	public byte[] handle(byte[] request) {
		if (request == null || request.length != this.requestLength()) {
			throw new RuntimeException("Wrong request length.");
		}
		this.requestValidation(request);
		return this.generateResponse(request);
	}

	public void requestValidation(byte[] request) {
		// do nothing
	}

	public abstract int requestLength();

	public abstract byte[] generateResponse(byte[] request);

	public byte[] generateResponseData(byte[] requestData) {
		byte functionCode = requestData[0];

		ModbusDataDefinition modbusDataDefinition = GlobalConfiguration.configuration.getModbusDataDefinition();

		if (modbusDataDefinition.getReturn06Exception()) {
			return this.exception(functionCode, ModbusExceptionCode.ERROR_06.getCode());
		}
		ModbusExceptionCode errorCode = null;
		byte[] data = null;
		short startAddress = DataUtils.bytesToShort(new byte[] { requestData[1], requestData[2] });
		short quality = DataUtils.bytesToShort(new byte[] { requestData[3], requestData[4] });
		if (quality >= 0x0001 || quality <= 0x07D0) {
			if (startAddress >= modbusDataDefinition.getAddress() && (startAddress
					+ quality) <= (modbusDataDefinition.getAddress() + modbusDataDefinition.getQuality())) {
				if (functionCode == 0x01) {
					if (modbusDataDefinition.getFunctionCode() != "01") {
						errorCode = ModbusExceptionCode.ERROR_01;
					} else {
						data = getCoilData(startAddress, quality);
					}
				} else if (functionCode == 0x02) {
					if (modbusDataDefinition.getFunctionCode() != "01") {
						errorCode = ModbusExceptionCode.ERROR_01;
					} else {
						data = getCoilData(startAddress, quality);
					}
				} else if (functionCode == 0x03) {
					if (modbusDataDefinition.getFunctionCode() != "01") {
						errorCode = ModbusExceptionCode.ERROR_01;
					} else {
						data = getRegisterData(startAddress, quality);
					}
				} else if (functionCode == 0x04) {
					if (modbusDataDefinition.getFunctionCode() != "01") {
						errorCode = ModbusExceptionCode.ERROR_01;
					} else {
						data = getRegisterData(startAddress, quality);
					}
				} else {
					errorCode = ModbusExceptionCode.ERROR_01;
				}
			} else {
				errorCode = ModbusExceptionCode.ERROR_02;
			}
		} else {
			errorCode = ModbusExceptionCode.ERROR_03;
		}
		if (errorCode != null) {
			return this.exception(functionCode, errorCode.getCode());
		} else {
			byte[] ret = new byte[data.length + 1];
			ret[0] = functionCode;
			System.arraycopy(data, 0, ret, 1, data.length);
			return ret;
		}

	}

	private byte[] getCoilData(short startAddress, short quality) {
		ModbusDataDefinition modbusDataDefinition = GlobalConfiguration.configuration.getModbusDataDefinition();

		StringBuffer sb = new StringBuffer();
		for (int index = (startAddress - modbusDataDefinition.getAddress()); index < (startAddress
				- modbusDataDefinition.getAddress() + quality); index++) {
			sb.append(DataStore.COILS_DATA.get(index));
		}

		int coilsCount = (sb.length() % 8 == 0) ? (sb.length() / 8) : (sb.length() / 8 + 1);

		byte[] ret = new byte[1 + coilsCount];
		ret[0] = (byte) coilsCount;

		for (int i = 0; i < coilsCount; i++) {
			int endIndex = i * 8 + 8;
			if (i * 8 + 8 > sb.length()) {
				endIndex = sb.length();
			}
			String byteStr = sb.toString().substring(i * 8, endIndex);
			String inverseByteStr = new StringBuffer(byteStr).reverse().toString();
			int byteData = Integer.parseInt(inverseByteStr, 2);
			ret[i + 1] = (byte) byteData;
		}

		return ret;
	}

	private byte[] getRegisterData(short startAddress, short quality) {
		ModbusDataDefinition modbusDataDefinition = GlobalConfiguration.configuration.getModbusDataDefinition();
		byte[] bytes = new byte[0];
		for (int index = (startAddress - modbusDataDefinition.getAddress()); index < (startAddress
				- modbusDataDefinition.getAddress() + quality); index++) {
			bytes = DataUtils.combine2ByteArrays(bytes, DataStore.REGISTER_DATA.get(index));
		}
		return bytes;
	}

	private byte[] exception(byte functionCode, byte errorCode) {
		return new byte[] { (byte) (functionCode + 0x80), errorCode };
	}
}
