
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by lihonghua on 2019/2/14.
 */
@EnableCaching //开启缓存
@ComponentScan(basePackages={"uyun"})//将项目中对应的Dao类的路径加进来就可以了
@EnableScheduling //开启任务调度
@SpringBootApplication
public class Startup {

    public static void main(String args[]){
//        EventConsumer eventConsumer = (EventConsumer) SpringUtil.getBean("eventConsumer");

        SpringApplication.run(Startup.class,args);
    }
}
