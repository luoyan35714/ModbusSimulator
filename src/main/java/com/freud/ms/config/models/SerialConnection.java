package com.freud.ms.config.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.freud.ms.config.models.base.Connection;
import com.freud.ms.config.models.enums.SerialConnectionParity;
import com.freud.ms.config.models.enums.SerialConnectionProtocolType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data()
@EqualsAndHashCode(callSuper = false)
@ToString
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SerialConnection extends Connection {

	private SerialConnectionProtocolType protocolType;
	private String comPort;
	private String baudrate;
	private String dataBits;
	private String stopBits;
	private SerialConnectionParity parity;

}
