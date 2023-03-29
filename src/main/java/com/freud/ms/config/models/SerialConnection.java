package com.freud.ms.config.models;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
@ToString
public class SerialConnection {

    private String protocolType;
    private String comPort;
    private String baudrate;
    private String dataBits;
	private String stopBits;
    private String parity;

}
