import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;



/**
 * @author GPX
 * @date 2019/12/9 13:32
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.guli.oss","com.guli.common"})
public class OssApplication {
    public static void main(String[] args){
        SpringApplication.run(OssApplication.class,args);
    }
}
