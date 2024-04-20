package com.freud.ms.server;

import com.freud.ms.config.GlobalConfiguration;
import com.freud.ms.protocol.ModBusASCIIParser;
import com.freud.ms.protocol.ModBusRTUParser;
import com.freud.ms.protocol.ModBusTCPParser;
import com.freud.ms.protocol.ProtocolHandler;
import com.freud.ms.util.DataUtils;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SocketServerDataHandler extends SimpleChannelInboundHandler<byte[]> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] bytes) throws Exception {

		try {
			log.info("request : " + DataUtils.bytesToHexString(bytes));

			ProtocolHandler dataHandler = null;

			switch (GlobalConfiguration.configuration.getProtocolType()) {
			case MODBUS_RTU:
				dataHandler = new ModBusRTUParser();
				break;
			case MODBUS_ASCII:
				dataHandler = new ModBusASCIIParser();
				break;
			case MODBUS_TCP:
			default:
				dataHandler = new ModBusTCPParser();
				break;
			}

			final byte[] response = dataHandler.handle(bytes);
			log.info("response : " + DataUtils.bytesToHexString(response));
			ctx.channel().writeAndFlush(response).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					String dataStr = DataUtils.bytesToHexString(response);
					StringBuilder sb = new StringBuilder("");
					if (future.isSuccess()) {
						log.info(sb.toString() + " response write back success " + dataStr);
					} else {
						log.error(sb.toString() + " response write back failed " + dataStr);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
