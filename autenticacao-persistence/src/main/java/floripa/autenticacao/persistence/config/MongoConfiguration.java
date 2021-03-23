package floripa.autenticacao.persistence.config;

import floripa.autenticacao.persistence.events.CascadeSaveMongoEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author brunno
 *
 */
@Configuration
public class MongoConfiguration {

    @Bean
    public CascadeSaveMongoEventListener cascadeSaveMongoEventListener(){
        return new CascadeSaveMongoEventListener();
    }

}


