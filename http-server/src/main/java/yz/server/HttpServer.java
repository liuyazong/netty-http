package yz.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * author: liuyazong
 * datetime: 2017/7/8 下午12:32
 */
@Component
@Slf4j
public class HttpServer {
    @Value("${server.port}")
    private int port;
    @Autowired
    private HttpRequestHandler httpRequestHandler;


    public void start() {
        int processors = Runtime.getRuntime().availableProcessors();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(processors);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(processors << 4);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .option(ChannelOption.SO_BACKLOG, 1024)
//                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(this.port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
//                                    .addLast(HttpRequestDecoder.class.getName(), new HttpRequestDecoder())
//                                    .addLast(HttpResponseEncoder.class.getName(), new HttpResponseEncoder())
//                                    //new HttpServerCodec() 等效于 new HttpRequestDecoder() + new HttpResponseEncoder()
                                    .addLast(HttpServerCodec.class.getName(), new HttpServerCodec())
//                                    .addLast(HttpServerKeepAliveHandler.class.getName(), new HttpServerKeepAliveHandler())
                                    .addLast(HttpObjectAggregator.class.getName(), new HttpObjectAggregator(65535))
                                    .addLast(ChunkedWriteHandler.class.getName(), new ChunkedWriteHandler())
                                    .addLast(httpRequestHandler.getClass().getName(), httpRequestHandler);
                        }
                    });

            ChannelFuture cf = serverBootstrap.bind().sync();
            log.debug("Server started on port {}", this.port);
            cf.channel()
                    .closeFuture()
                    .addListener(new GenericFutureListener<Future<? super Void>>() {
                        @Override
                        public void operationComplete(Future<? super Void> future) throws Exception {
                            bossGroup.shutdownGracefully().sync();
                            workerGroup.shutdownGracefully().sync();
                            log.debug("Server shutdown thread group {},{}", bossGroup, workerGroup);
                        }
                    })
                    .sync();
            log.debug("Server closed on port {}", this.port);
        } catch (Exception e) {
            log.error("An error occurred {}", e);
        }
    }

}
