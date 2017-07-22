import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import yz.config.AppConfig;
import yz.server.JerseyHttpServer;

/**
 * author: liuyazong
 * datetime: 2017/7/8 下午2:01
 */
public class HttpServerApplication {
    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        context.start();
//        HttpServer httpServer = context.getBean(HttpServer.class);
//        httpServer.start();
        JerseyHttpServer server = context.getBean(JerseyHttpServer.class);
        server.start();
    }
}
