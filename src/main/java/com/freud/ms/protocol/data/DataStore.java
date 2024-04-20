package com.freud.ms.protocol.data;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.freud.ms.config.GlobalConfiguration;
import com.freud.ms.config.models.ModbusData;
import com.freud.ms.config.models.ModbusDataDefinition;
import com.freud.ms.config.models.enums.ModbusDataStatusValue;
import com.freud.ms.util.DataUtils;

public final class DataStore extends Thread {

	public static final Map<Integer, Byte> COILS_DATA = new ConcurrentHashMap<>();

	public static final Map<Integer, byte[]> REGISTER_DATA = new ConcurrentHashMap<>();

	@Override
	public void run() {
		while (true) {
			try {
				update();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void update() {
		ModbusDataDefinition definition = GlobalConfiguration.configuration.getModbusDataDefinition();
		if (definition.getFunctionCode().equals("01") || definition.getFunctionCode().equals("02")) {
			int index = 0;
			for (ModbusData modbusData : GlobalConfiguration.configuration.getModbusDataList()) {
				Byte b = COILS_DATA.get(index);
				if (b == null) {
					b = (byte) (modbusData.getStatusValue().equals(ModbusDataStatusValue.on) ? 1 : 0);
				} else {
					if (modbusData.getAutoChange()) {
						b = (byte) (~b & 0x01);
					} else {
						index++;
						continue;
					}
				}
				COILS_DATA.put(index++, b);
			}
		} else {
			int index = 0;
			for (ModbusData modbusData : GlobalConfiguration.configuration.getModbusDataList()) {
				byte[] bytes = REGISTER_DATA.get(index);
				if (bytes == null) {
					bytes = DataUtils.shortToBytes(modbusData.getValue().shortValue());
				} else {
					if (modbusData.getRandom()) {
						Integer integer = new Random().nextInt();
						bytes = DataUtils.shortToBytes(integer.shortValue());
					} else if (modbusData.getAutoIncrement()) {
						short existing = DataUtils.bytesToShort(bytes);
						bytes = DataUtils.shortToBytes(Integer.valueOf(existing + 1).shortValue());
					} else {
						index++;
						continue;
					}
				}
				REGISTER_DATA.put(index++, bytes);
			}
		}
	}
}
