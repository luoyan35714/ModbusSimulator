package com.freud.ms.config.models;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.freud.ms.config.models.enums.ModbusProtocolType;

import lombok.Data;

@Data
@XmlRootElement(name = "modbusSimulator")
@XmlAccessorType(XmlAccessType.FIELD)
public class ModbusSimulatorVO {

	private ModbusProtocolType protocolType;

	private SerialConnection serialConnection;
	private TcpConnection tcpConnection;
	private ModbusDataDefinition modbusDataDefinition;

	@XmlElement(name = "modbusData")
	@XmlElementWrapper(name = "modbusDataList")
	private List<ModbusData> modbusDataList;

}
