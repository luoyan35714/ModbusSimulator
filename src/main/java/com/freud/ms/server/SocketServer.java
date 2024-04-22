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
		// receiving client connections
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		// processing data
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		try {
			// Create a ServerBootstrap instance to boot and bind the server
			serverBootstrap.group(bossGroup, workerGroup)
					// Non blocking
					.channel(NioServerSocketChannel.class)
					// The size of the connection buffer pool
					.option(ChannelOption.SO_BACKLOG, 1024)
					// Set up the allocator for channel channels
					.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							// Set timeout
							pipeline.addLast(new IdleStateHandler(30, 30, 60, TimeUnit.SECONDS));

							// Add codec and processor (for inter node communication)
							pipeline.addLast("bdeCoder", new ByteArrayDecoder());
							pipeline.addLast("benCoder", new ByteArrayEncoder());

							// Add ChannelHandler to handle client data
							pipeline.addLast(new SocketServerDataHandler());

							pipeline.addLast("exception", new NettyExceptionHandler());
						}
					});

			ChannelFuture future = serverBootstrap.bind(port).sync();
			log.info("netty started on : " + port);
			future.channel().closeFuture().sync();
		} finally {
			log.info("netty stoped...");
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
