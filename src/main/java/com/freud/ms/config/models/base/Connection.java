package com.freud.ms.config.models.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.freud.ms.config.models.SerialConnection;
import com.freud.ms.config.models.TcpConnection;

@XmlSeeAlso({ SerialConnection.class, TcpConnection.class })
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Connection {

}
