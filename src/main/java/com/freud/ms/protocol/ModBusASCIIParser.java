package com.freud.ms.protocol;

import java.util.Arrays;

import com.freud.ms.util.DataUtils;
import com.freud.ms.util.LRC;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModBusASCIIParser extends ProtocolHandler {

	private static final String START_STR = "3A";
	private static final String END_STR = "0D0A";

	private static final int ADU_START_LENGTH = 1;
	private static final int ADU_SLAVE_LENGTH = 2;
	private static final int PDU_FUNCTION_LENGTH = 2;
	private static final int PDU_START_ADDRESS_LENGTH = 4;
	private static final int PDU_QUALITY_LENGTH = 4;
	private static final int ADU_LRC_LENGTH = 2;
	private static final int ADU_END_LENGTH = 2;

	@Override
	public int requestLength() {
		return ADU_START_LENGTH + ADU_SLAVE_LENGTH + PDU_FUNCTION_LENGTH + PDU_START_ADDRESS_LENGTH + PDU_QUALITY_LENGTH
				+ ADU_LRC_LENGTH + ADU_END_LENGTH;
	}

	@Override
	public byte[] generateResponse(byte[] request) {

		// Remove start_str, lrc, end_str
		byte[] requestASCIIData = Arrays.copyOfRange(request, 1, request.length - 4);
		// convert to bytes instead of ASCII
		byte[] requestRealData = DataUtils.hexStrToBytes(new String(requestASCIIData));
		// remove
		byte[] requestData = Arrays.copyOfRange(requestRealData, ADU_SLAVE_LENGTH / 2, requestRealData.length);
		byte[] responseData = this.generateResponseData(requestData);
		byte[] response = new byte[(ADU_SLAVE_LENGTH + PDU_FUNCTION_LENGTH) / 2 + responseData.length];

		// slave
		response[0] = requestRealData[0];
		// funcation code
		response[1] = requestRealData[1];

		System.arraycopy(responseData, 0, response, 2, responseData.length);

		String responseStr = DataUtils.bytesToHexString(response);
		String lrc = LRC.lrc(responseStr);

		return (START_STR + responseStr + lrc + END_STR).getBytes();
	}

	@Override
	public void requestValidation(byte[] request) {

		if (request[0] != 0x3A) {
			throw new RuntimeException("Request must start with [" + START_STR + "]");
		}

		if (request[request.length - 2] != 0x0D || request[request.length - 1] != 0x0A) {
			throw new RuntimeException("Request must end with [" + END_STR + "]");
		}

		byte[] requestData = new byte[request.length - ADU_START_LENGTH - ADU_LRC_LENGTH - ADU_END_LENGTH];
		System.arraycopy(request, 1, requestData, 0, requestData.length);
		// LRC
		String lrc = LRC.lrc(new String(requestData));
		String requestLrc = new String(Arrays.copyOfRange(request, request.length - 4, request.length - 2));
		if (!lrc.equals(requestLrc)) {
			log.error("Request LRC validation failed. Calculated LRC is: " + lrc);
			throw new RuntimeException("Request LRC validation failed.");
		}
	}
}
