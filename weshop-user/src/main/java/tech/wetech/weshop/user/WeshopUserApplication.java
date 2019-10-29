package tech.wetech.weshop.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

//@SpringCloudApplication
//@EnableWebMvc
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("tech.wetech.weshop.*.api")
@ComponentScan(value = "tech.wetech.weshop")
@MapperScan(basePackages = "tech.wetech.weshop.*.mapper")
//@EnableCaching
public class WeshopUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeshopUserApplication.class, args);
    }

}
