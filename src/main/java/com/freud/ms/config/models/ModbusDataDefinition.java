package com.freud.ms.config.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ModbusDataDefinition {

	private Integer slaveId;
	private String functionCode;
	private Integer address;
	private Integer quality;
	private Boolean return06Exception = false;

}
