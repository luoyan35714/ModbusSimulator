package com.freud.ms.exception;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty Exception handler
 *
 */
@ChannelHandler.Sharable
@Slf4j
public class NettyExceptionHandler extends ChannelInboundHandlerAdapter {

	/**
	 * Exception handler
	 * 
	 * @param ctx
	 * @param cause
	 * @throws Exception
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause instanceof Exception) {
			cause.printStackTrace();
			log.error(cause.getMessage());
			ctx.close();
		} else {
			cause.printStackTrace();
			log.error(cause.getMessage());
			ctx.close();
		}
	}

	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
}
