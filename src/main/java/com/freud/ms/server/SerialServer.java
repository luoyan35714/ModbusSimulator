package com.freud.ms.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.freud.ms.config.GlobalConfiguration;
import com.freud.ms.config.models.SerialConnection;
import com.freud.ms.protocol.ModBusASCIIParser;
import com.freud.ms.protocol.ModBusRTUParser;
import com.freud.ms.protocol.ModBusTCPParser;
import com.freud.ms.protocol.ProtocolHandler;
import com.freud.ms.util.DataUtils;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import lombok.extern.slf4j.Slf4j;

/**
 * Serial server and data handler
 * 
 * @author Freud
 *
 */
@Slf4j
public class SerialServer extends ModbusSimulatorServer implements SerialPortEventListener {
	private SerialPort serialPort;
	private InputStream inputStream;
	private OutputStream outputStream;

	public void startServer() throws Exception {

		SerialConnection connection = GlobalConfiguration.configuration.getSerialConnection();

		// Obtain a serial communication instance
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(connection.getComPort());
		// Open serial port
		serialPort = (SerialPort) portIdentifier.open("SerialServer", 2000);
		// Set serial communication parameters
		serialPort.setSerialPortParams(connection.getBaudrate(), connection.getDataBits(), connection.getStopBits(),
				connection.getParity().getValue());

		inputStream = serialPort.getInputStream();
		outputStream = serialPort.getOutputStream();
		// Add a serial event listener
		serialPort.addEventListener(this);
		serialPort.notifyOnDataAvailable(true);
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				int availableBytes = inputStream.available();
				byte[] bytes = new byte[availableBytes];
				inputStream.read(bytes, 0, availableBytes);
				log.info("Reuest : " + DataUtils.bytesToHexString(bytes));

				ProtocolHandler dataHandler = null;

				switch (GlobalConfiguration.configuration.getProtocolType()) {
				case MODBUS_RTU:
					dataHandler = new ModBusRTUParser();
					break;
				case MODBUS_ASCII:
					dataHandler = new ModBusASCIIParser();
					break;
				case MODBUS_TCP:
				default:
					dataHandler = new ModBusTCPParser();
					break;
				}

				final byte[] response = dataHandler.handle(bytes);
				String responseStr = DataUtils.bytesToHexString(response);
				log.info("response : " + responseStr);

				outputStream.write(response);
				Thread.sleep(1000);
				outputStream.flush();
				log.info("response write back success " + responseStr);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}