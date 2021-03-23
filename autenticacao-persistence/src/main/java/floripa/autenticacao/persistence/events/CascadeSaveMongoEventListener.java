package floripa.autenticacao.persistence.events;

import floripa.autenticacao.persistence.events.reflection.CascadeCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author brunno
 *
 */
public class CascadeSaveMongoEventListener extends AbstractMongoEventListener<Object> {

    private static final Logger logger = LoggerFactory.getLogger(CascadeSaveMongoEventListener.class);

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Object> event) {
        logger.info("cascade save");
        Object source = event.getSource();
        ReflectionUtils.doWithFields(source.getClass(), new CascadeCallback(source, mongoOperations));

    }
}
