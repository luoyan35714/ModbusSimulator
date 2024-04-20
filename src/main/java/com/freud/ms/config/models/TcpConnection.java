package com.freud.ms.config.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.freud.ms.config.models.base.Connection;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TcpConnection extends Connection {

	private Integer port;

}
