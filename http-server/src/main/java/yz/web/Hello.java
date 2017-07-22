package yz.web;

import org.springframework.context.ApplicationContext;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * author: liuyazong
 * datetime: 2017/7/20 下午3:17
 */
@Path("hello")
public class Hello {

    @GET
    @Path("{sth}")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@PathParam(value = "sth") String sth,
                        @QueryParam("a") List<String> a,
                        @QueryParam("b") String b) {
        return "Hello,".concat(sth.toUpperCase()).concat(",a=" + a).concat(",b=" + b);
    }
}
