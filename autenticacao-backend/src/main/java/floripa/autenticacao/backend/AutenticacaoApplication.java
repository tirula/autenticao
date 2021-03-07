package floripa.autenticacao.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 
 * @author brunno
 *
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableAsync 
@EnableCaching
@EnableSpringDataWebSupport
public class AutenticacaoApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(AutenticacaoApplication.class, args);
	}
	
	

}
