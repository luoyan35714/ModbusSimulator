package com.freud.ms.config.models;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TcpConnection {

    private String protocolType;
    private Integer port;

}
