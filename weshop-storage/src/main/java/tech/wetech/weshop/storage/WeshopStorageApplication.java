package tech.wetech.weshop.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

//@SpringCloudApplication
//@EnableWebMvc
@SpringBootApplication
@EnableDiscoveryClient
//@EnableSwagger2
@ComponentScan(value = "tech.wetech.weshop")
@EnableFeignClients("tech.wetech.weshop.*.api")
//@EnableCaching
@MapperScan(basePackages = "tech.wetech.weshop.*.mapper")
public class WeshopStorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeshopStorageApplication.class, args);
	}

}
