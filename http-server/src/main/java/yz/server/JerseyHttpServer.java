package yz.server;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import yz.web.Hello;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * author: liuyazong
 * datetime: 2017/7/20 下午4:47
 */
@Slf4j
@Component
public class JerseyHttpServer {

    @Value("${server.port}")
    private int port;

    public void start() throws InterruptedException {
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(this.port).build();
        ResourceConfig resourceConfig = new ResourceConfig(Hello.class);
        Channel server = NettyHttpContainerProvider.createServer(baseUri, resourceConfig, false);
        log.debug("Server port:{},isOpen:{}", this.port, server.isOpen());
        log.debug("Server port:{},isActive:{}", this.port, server.isActive());
        server.closeFuture().sync();
    }
}
