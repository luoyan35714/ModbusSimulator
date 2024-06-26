package com.freud.ms.config;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.freud.ms.config.models.ModbusData;
import com.freud.ms.config.models.ModbusDataDefinition;
import com.freud.ms.config.models.SerialConnection;
import com.freud.ms.config.models.TcpConnection;
import com.freud.ms.config.models.enums.ModbusProtocolType;

public final class ConfigurationValidation {

	public static void validateModbusSerialConfig() {
		ConfigurationValidation.validateModbusProtocolType();
		
		SerialConnection connection = GlobalConfiguration.configuration.getSerialConnection();

		if (StringUtils.isBlank(connection.getComPort())) {
			throw new RuntimeException("Please define the com port.");
		}

		if (connection.getBaudrate() == null
				|| (!connection.getBaudrate().equals(300)
				&& !connection.getBaudrate().equals(600) 
				&& !connection.getBaudrate().equals(1200)
				&& !connection.getBaudrate().equals(4800) 
				&& !connection.getBaudrate().equals(9600)
				&& !connection.getBaudrate().equals(14400) 
				&& !connection.getBaudrate().equals(19200)
				&& !connection.getBaudrate().equals(38400) 
				&& !connection.getBaudrate().equals(56000)
				&& !connection.getBaudrate().equals(57600) 
				&& !connection.getBaudrate().equals(115200))) {
			throw new RuntimeException(
					"Baudrate can only be [300, 600, 1200, 2400, 4800, 9600, 14400, 19200, 38400, 56000, 57600, 115200].");
		}

		if (connection.getDataBits() == null
				|| (!connection.getDataBits().equals(7) 
						&& !connection.getDataBits().equals(8))) {
			throw new RuntimeException("DataBits can only be [7, 8].");
		}

		if (connection.getStopBits() == null
				|| (!connection.getStopBits().equals(1) 
						&& !connection.getStopBits().equals(2))) {
			throw new RuntimeException("StopBits can only be [1, 2].");
		}

		if (connection.getParity() == null) {
			throw new RuntimeException("Please define the Parity by [NONE, ODD, EVEN].");
		}

		ConfigurationValidation.validateModbusDataDefinition();
		ConfigurationValidation.validateModbusData();
	}

	public static void validateModbusTCPConfig() {
		ConfigurationValidation.validateModbusProtocolType();
		
		TcpConnection connection = GlobalConfiguration.configuration.getTcpConnection();
		
		if (connection.getPort() == null || connection.getPort() <= 0 || connection.getPort() > 65535) {
			throw new RuntimeException("TCP Connection port can only be a number between (0, 65535]");
		}

		ConfigurationValidation.validateModbusDataDefinition();
		ConfigurationValidation.validateModbusData();
	}
	
	
	public static void validateModbusProtocolType() {
		ModbusProtocolType protocolType = GlobalConfiguration.configuration.getProtocolType();
		if (protocolType == null) {
			throw new RuntimeException("Please define the protocol type by [MODBUS_TCP, MODBUS_RTU, MODBUS_ASCII]");
		}
	}

	public static void validateModbusDataDefinition() {
		ModbusDataDefinition dataDefinition = GlobalConfiguration.configuration.getModbusDataDefinition();
		if (dataDefinition == null) {
			throw new RuntimeException("Please define the modbus data definition.");
		}

		if (dataDefinition.getSlaveId() == null || dataDefinition.getSlaveId() < 1
				|| dataDefinition.getSlaveId() > 255) {
			throw new RuntimeException("SlaveId can only be a number between [1, 255]");
		}

		if (StringUtils.isEmpty(dataDefinition.getFunctionCode()) 
				|| (!dataDefinition.getFunctionCode().equals("01")
				&& !dataDefinition.getFunctionCode().equals("02") 
				&& !dataDefinition.getFunctionCode().equals("03")
				&& !dataDefinition.getFunctionCode().equals("04"))) {
			throw new RuntimeException("Function code can only be [01, 02, 03, 04].");
		}

		if (dataDefinition.getAddress() == null || dataDefinition.getAddress() < 0
				|| dataDefinition.getAddress() > 65535) {
			throw new RuntimeException("Address can only be a number between [0, 65535]");
		}
		if (dataDefinition.getQuality() == null || dataDefinition.getQuality() < 1
				|| dataDefinition.getQuality() > 4096) {
			throw new RuntimeException("Quality can only be a number between [1, 4096]");
		}

	}

	public static void validateModbusData() {
		List<ModbusData> modbusDatas = GlobalConfiguration.configuration.getModbusDataList();
		if (CollectionUtils.isEmpty(modbusDatas)
				|| modbusDatas.size() != GlobalConfiguration.configuration.getModbusDataDefinition().getQuality()) {
			throw new RuntimeException("please define ["
					+ GlobalConfiguration.configuration.getModbusDataDefinition().getQuality() + "] modbus data.");
		} else {
			modbusDatas.stream().forEach(modbusData -> {
				// Coils
				if (GlobalConfiguration.configuration.getModbusDataDefinition().getFunctionCode().equals("03")
						|| GlobalConfiguration.configuration.getModbusDataDefinition().getFunctionCode().equals("04")) {
					if (modbusData.getRandom() != null && modbusData.getAutoIncrement() != null
							&& modbusData.getRandom() && modbusData.getAutoIncrement()) {
						throw new RuntimeException(
								"Only one of Random and AutoIncrement can be true within one modbus data.");
					}
				}
			});

		}
	}
}
