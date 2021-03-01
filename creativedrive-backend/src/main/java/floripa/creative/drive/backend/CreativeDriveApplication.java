package floripa.creative.drive.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 
 * @author brunno
 *
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableAsync 
@EnableCaching
public class CreativeDriveApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(CreativeDriveApplication.class, args);
	}
	
	

}
