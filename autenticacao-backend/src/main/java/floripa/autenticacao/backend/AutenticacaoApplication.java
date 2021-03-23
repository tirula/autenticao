package floripa.autenticacao.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 
 * @author brunno
 *
 */

@SpringBootApplication(scanBasePackages={
		"floripa.autenticacao.persistence",
		"floripa.autenticacao.security",
		"floripa.autenticacao.backend"})
@EnableMongoRepositories(basePackages = {"floripa.autenticacao.persistence"})
@EnableAsync 
@EnableCaching
@EnableSpringDataWebSupport
public class AutenticacaoApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(AutenticacaoApplication.class, args);
	}
	
	

}
