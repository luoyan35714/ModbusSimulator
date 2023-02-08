package com.freud.modbus.netty;

import javax.annotation.Resource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 监听Spring容器启动完成，完成后启动Netty服务器
 **/
@Component
public class NettyStartListener implements ApplicationRunner {
	@Resource
	private SocketServer socketServer;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		this.socketServer.start();
	}
}
