package com.freud.ms.server;

import java.util.concurrent.TimeUnit;

import com.freud.ms.exception.NettyExceptionHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SocketServer extends ModbusSimulatorServer {

	private int port;

	public SocketServer(int port) {
		this.port = port;
	}

	public void startServer() throws Exception {
		// 创建两个EventLoopGroup，一个用于接收客户端连接，另一个用于处理数据
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		try {
			// 创建ServerBootstrap实例来引导和绑定服务器
			serverBootstrap.group(bossGroup, workerGroup)
					// 非阻塞
					.channel(NioServerSocketChannel.class)
					// 连接缓冲池的大小
					.option(ChannelOption.SO_BACKLOG, 1024)
					// 设置通道Channel的分配器
					.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							// 设置超时
							pipeline.addLast(new IdleStateHandler(30, 30, 60, TimeUnit.SECONDS));

							// 添加编解码和处理器(节点间通讯用)
							pipeline.addLast("bdeCoder", new ByteArrayDecoder());
							pipeline.addLast("benCoder", new ByteArrayEncoder());

							// 添加ChannelHandler以处理客户端数据
							pipeline.addLast(new SocketServerDataHandler());

							// 异常处理
							pipeline.addLast("exception", new NettyExceptionHandler());
						}
					});

			// 绑定端口并开始接收连接
			ChannelFuture future = serverBootstrap.bind(port).sync();
			log.info("netty started on : " + port);
			// 等待服务器套接字关闭
			future.channel().closeFuture().sync();
		} finally {
			log.info("netty stoped...");
			// 关闭EventLoopGroup以释放所有资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
