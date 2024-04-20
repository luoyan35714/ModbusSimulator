package com.freud.ms.protocol;

import java.util.Arrays;

import com.freud.ms.util.CRC16;
import com.freud.ms.util.DataUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModBusRTUParser extends ProtocolHandler {

	private static final int ADU_SLAVE_LENGTH = 1;
	private static final int PDU_FUNCTION_LENGTH = 1;
	private static final int PDU_STARTING_ADDRESS_LENGTH = 2;
	private static final int PDU_QUALITY_LENGTH = 2;
	private static final int ADU_CRC_LENGTH = 2;

	@Override
	public int requestLength() {
		return ADU_SLAVE_LENGTH + PDU_FUNCTION_LENGTH + PDU_STARTING_ADDRESS_LENGTH + PDU_QUALITY_LENGTH
				+ ADU_CRC_LENGTH;
	}

	@Override
	public void requestValidation(byte[] request) {
		// CRC
		byte[] crcData = Arrays.copyOfRange(request, 0, request.length - 2);
		byte[] crc = CRC16.getCRC(crcData);
		if (crc[0] != request[request.length - 2] || crc[1] != request[request.length - 1]) {
			log.error("Request CRC validation failed. Calculated CRC is: " + DataUtils.bytesToHexString(crc));
			throw new RuntimeException("Request CRC validation failed.");
		}
	}

	@Override
	public byte[] generateResponse(byte[] request) {

		int dataIndex = ADU_SLAVE_LENGTH;
		byte[] requestData = Arrays.copyOfRange(request, dataIndex, request.length - ADU_CRC_LENGTH);
		byte[] responseData = this.generateResponseData(requestData);
		byte[] response = new byte[ADU_SLAVE_LENGTH + PDU_FUNCTION_LENGTH + responseData.length + ADU_CRC_LENGTH];

		// slave
		response[0] = request[0];
		// funcation code
		response[1] = request[1];

		System.arraycopy(responseData, 0, response, dataIndex, responseData.length);

		byte[] crcData = Arrays.copyOfRange(response, 0, response.length - 2);
		byte[] crc = CRC16.getCRC(crcData);

		// CRC
		response[response.length - 2] = crc[0];
		response[response.length - 1] = crc[1];
		return response;
	}

}
