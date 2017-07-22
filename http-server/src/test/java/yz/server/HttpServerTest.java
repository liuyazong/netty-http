package yz.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import yz.config.AppConfig;

/**
 * author: liuyazong
 * datetime: 2017/7/8 下午12:49
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class HttpServerTest {
    @Autowired
    private HttpServer httpServer;
    @Autowired
    private JerseyHttpServer jerseyHttpServer;

    @Test
    public void test() throws InterruptedException {
//        httpServer.start();
        jerseyHttpServer.start();
    }

}
