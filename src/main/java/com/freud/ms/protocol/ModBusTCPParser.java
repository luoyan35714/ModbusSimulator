package com.freud.ms.protocol;

import java.util.Arrays;

import com.freud.ms.config.GlobalConfiguration;
import com.freud.ms.util.DataUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModBusTCPParser extends ProtocolHandler {

	private static final int MBAP_TRANSACTION_LENGTH = 2;
	private static final int MBAP_PROTOCOL_LENGTH = 2;
	private static final int MBAP_LENGTH_LENGTH = 2;
	private static final int MBAP_SLAVE_LENGTH = 1;
	private static final int PDU_FUNCTION_LENGTH = 1;
	private static final int PDU_STARTING_ADDRESS_LENGTH = 2;
	private static final int PDU_QUALITY_LENGTH = 2;

	@Override
	public void requestValidation(byte[] request) {
		byte slaveId = request[6];
		if (!GlobalConfiguration.configuration.getModbusDataDefinition().getSlaveId()
				.equals(Integer.valueOf(slaveId))) {
			log.error("Request slaveId not match! configured:["
					+ GlobalConfiguration.configuration.getModbusDataDefinition().getSlaveId() + "], requested:["
					+ slaveId + "]");
			throw new RuntimeException("Request slaveId not match!");
		}
	}

	@Override
	public int requestLength() {
		return MBAP_TRANSACTION_LENGTH + MBAP_PROTOCOL_LENGTH + MBAP_LENGTH_LENGTH + MBAP_SLAVE_LENGTH
				+ PDU_FUNCTION_LENGTH + PDU_STARTING_ADDRESS_LENGTH + PDU_QUALITY_LENGTH;
	}

	@Override
	public byte[] generateResponse(byte[] request) {

		int dataIndex = MBAP_TRANSACTION_LENGTH + MBAP_PROTOCOL_LENGTH + MBAP_LENGTH_LENGTH + MBAP_SLAVE_LENGTH;
		byte[] requestData = Arrays.copyOfRange(request, dataIndex, request.length);
		byte[] responseData = this.generateResponseData(requestData);

		byte[] response = new byte[MBAP_TRANSACTION_LENGTH + MBAP_PROTOCOL_LENGTH + MBAP_LENGTH_LENGTH
				+ MBAP_SLAVE_LENGTH + responseData.length];

		// transaction identifier
		response[0] = request[0];
		response[1] = request[1];
		// protocol identifier
		response[2] = request[2];
		response[3] = request[3];

		int length = MBAP_SLAVE_LENGTH + responseData.length;

		byte[] lengthArray = DataUtils.shortToBytes((short) length);

		// length
		response[4] = lengthArray[0];
		response[5] = lengthArray[1];
		// slave
		response[6] = request[6];

		System.arraycopy(responseData, 0, response, dataIndex, responseData.length);
		return response;
	}

}
