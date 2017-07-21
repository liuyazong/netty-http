package yz.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.internal.PropertiesDelegate;
import org.glassfish.jersey.netty.connector.internal.NettyInputStream;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ContainerRequest;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.SecurityContext;
import java.net.URI;
import java.security.Principal;
import java.util.*;

/**
 * author: liuyazong
 * datetime: 2017/7/8 下午12:32
 */
@Component
@ChannelHandler.Sharable
@Slf4j
public class HttpRequestHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest) msg;
        String uri = request.uri();
        String path = null;
        Map<String, List<String>> parameters = null;
        if (HttpMethod.GET.equals(request.method())) {
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
            parameters = queryStringDecoder.parameters();
            path = queryStringDecoder.path();
        } else if (HttpMethod.POST.equals(request.method())) {
            path = uri;
        }
        String content = request.content().toString(CharsetUtil.UTF_8);
        log.debug("http request path:{},data:{},data:{}", path, parameters, content);
        FullHttpResponse response = buildResponse(request, Unpooled.copiedBuffer(uri.getBytes()));
        ctx.writeAndFlush(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("some error occurred: {}", cause);
        ctx.close();
    }

    private FullHttpResponse buildResponse(HttpRequest request, ByteBuf byteBuf) {

        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
        HttpHeaders headers = response.headers();
        if (request.headers().get(HttpHeaderNames.CONNECTION).equals(HttpHeaderValues.KEEP_ALIVE)) {
            headers.add(HttpHeaderNames.CONNECTION.toString(), HttpHeaderValues.KEEP_ALIVE.toString());
        } else {
            headers.add(HttpHeaderNames.CONNECTION.toString(), HttpHeaderValues.CLOSE.toString());
        }

        return response;
    }
}
