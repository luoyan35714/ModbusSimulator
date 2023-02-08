package com.freud.modbus.netty;

import org.springframework.stereotype.Component;

import com.freud.modbus.tools.DBUSModbusUtil;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * Socket拦截器，用于处理客户端的行为
 **/
@Slf4j
@Component
@Sharable
public class SocketHandler extends ChannelInboundHandlerAdapter {
	public static final ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

//	@Autowired
//	private DataSender dataSender;

	/**
	 * 读取到客户端发来的消息
	 *
	 * @param ctx ChannelHandlerContext
	 * @param msg msg
	 * @throws Exception e
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 由于我们配置的是 字节数组 编解码器，所以这里取到的用户发来的数据是 byte数组
		byte[] data = (byte[]) msg;
		System.out.println(DBUSModbusUtil.bytesToHexString(data));
//		dataSender.send(msgInfo);

	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		log.info("新的客户端链接：" + ctx.channel().id().asShortText());
		clients.add(ctx.channel());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		clients.remove(ctx.channel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.channel().close();
		clients.remove(ctx.channel());
	}
}
