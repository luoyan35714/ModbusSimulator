package com.freud.ms.config.models;

import java.util.List;

public class ModbusSimulatorVO {

    private SerialConnection serialConnection;

    private TcpConnection tcpConnection;

    private ModbusDataDefinition modbusDataDefinition;

    private List<ModbusData> modbusDataList;
}
