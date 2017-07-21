package yz.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * author: liuyazong
 * datetime: 2017/7/20 下午3:17
 */
@Path("test")
public class Hello {

    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@QueryParam(value = "sth") String sth) {
        String result = "Hello,".concat(sth.toUpperCase());
        return result;
    }
}
