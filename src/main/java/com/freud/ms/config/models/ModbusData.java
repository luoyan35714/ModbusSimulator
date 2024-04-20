package com.freud.ms.config.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.freud.ms.config.models.enums.ModbusDataStatusValue;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@XmlRootElement(name = "modbusData")
@XmlAccessorType(XmlAccessType.FIELD)
public class ModbusData {
	private Integer index;
	private Boolean autoChange = false;
	private ModbusDataStatusValue statusValue = ModbusDataStatusValue.off;

	private Boolean random = false;
	private Boolean autoIncrement = false;
	private Integer value;
}
