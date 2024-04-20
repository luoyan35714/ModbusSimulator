package com.freud.ms.server;

import com.freud.ms.protocol.data.DataStore;

public abstract class ModbusSimulatorServer {

	public void start() throws Exception {
		new DataStore().start();
		this.startServer();
	}

	abstract void startServer() throws Exception;
}
