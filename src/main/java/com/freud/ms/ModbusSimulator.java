package com.freud.ms;

import com.freud.ms.config.ConfigurationValidation;
import com.freud.ms.config.GlobalConfiguration;
import com.freud.ms.config.models.ModbusSimulatorVO;
import com.freud.ms.server.SerialServer;
import com.freud.ms.server.SocketServer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModbusSimulator {

	public static void main(String[] args) throws Exception {
		GlobalConfiguration.load();
		ModbusSimulatorVO config = GlobalConfiguration.configuration;
		if (config.getTcpConnection() != null && config.getSerialConnection() != null) {
			throw new RuntimeException(
					"You can only define one connection(serial connection or tcp connection) in the sametime.");
		} else if (config.getTcpConnection() != null) {
			ConfigurationValidation.validateModbusTCPConfig();
			Integer port = config.getTcpConnection().getPort();
			if (port != null && port > 0 && port <= 65535) {
				log.info("TCP IP connection on [" + port + "] is starting.");
				new SocketServer(port).start();
			} else {
				throw new RuntimeException("Please make sure you are using the correct TCP/IP port.");
			}
		} else if (config.getSerialConnection() != null) {
			ConfigurationValidation.validateModbusSerialConfig();
			log.info("Serial connection on [" + config.getSerialConnection().getComPort() + "] is starting.");
			new SerialServer().start();
		} else {
			throw new RuntimeException(
					"Please define Serial or TCP/IP connection information in the configuration file["
							+ GlobalConfiguration.CONFIG_FILE_NAME + "].");
		}

	}
}
