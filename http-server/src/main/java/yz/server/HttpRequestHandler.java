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
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String path = "";
        Map<String, List<String>> parameters = new HashMap<>();
        if (HttpMethod.GET.equals(request.method())) {
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
            parameters = queryStringDecoder.parameters();
            path = queryStringDecoder.path();
        } else if (HttpMethod.POST.equals(request.method())) {
            path = uri;
        }
        String content = request.content().toString(CharsetUtil.UTF_8);
        log.debug("http request path:{},params:{},content:{}", path, parameters, content);
        FullHttpResponse response = buildResponse(request, Unpooled.copiedBuffer(uri.getBytes()));
        ctx.writeAndFlush(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
        log.debug("channelReadComplete:{}", ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("some error occurred: {}", cause);
        ctx.close();
    }

    private FullHttpResponse buildResponse(HttpRequest request, ByteBuf byteBuf) {
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        log.debug("channelRegistered:{}", ctx.channel().localAddress());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        log.debug("channelUnregistered:{}", ctx.channel().localAddress());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.debug("channelActive:{}", ctx.channel().localAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.debug("channelInactive:{}", ctx.channel().localAddress());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        log.debug("userEventTriggered:{},evt:{}", ctx.channel().localAddress(), evt);
    }
}
