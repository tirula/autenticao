package floripa.autenticacao.persistence.events.reflection;

import floripa.autenticacao.persistence.events.annotations.CascadeSave;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CascadeCallback implements ReflectionUtils.FieldCallback {

    private Object source;
    private MongoOperations mongoOperations;

    public CascadeCallback(final Object source, final MongoOperations mongoOperations) {
        this.source = source;
        this.setMongoOperations(mongoOperations);
    }

    @Override
    public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
        ReflectionUtils.makeAccessible(field);
        if (field.isAnnotationPresent(DBRef.class) && field.isAnnotationPresent(CascadeSave.class)) {
            if (field.getType() == List.class || field.getType() == ArrayList.class) {
                List collection = (List) field.get(getSource());
                if (collection != null) {
                    Iterator iterator = collection.iterator();
                    while (iterator.hasNext()) {
                        final Object fieldValue = iterator.next();
                        if (fieldValue != null) {
                            final FieldCallback callback = new FieldCallback();
                            ReflectionUtils.doWithFields(fieldValue.getClass(), callback);
                            getMongoOperations().save(fieldValue);
                        }
                    }
                }
            } else {
                // handle individual object
                final Object fieldValue = field.get(getSource());
                if (fieldValue != null) {
                    final FieldCallback callback = new FieldCallback();
                    ReflectionUtils.doWithFields(fieldValue.getClass(), callback);
                    getMongoOperations().save(fieldValue);
                }
            }
        }

    }

    private Object getSource() {
        return source;
    }

    public void setSource(final Object source) {
        this.source = source;
    }

    private MongoOperations getMongoOperations() {
        return mongoOperations;
    }

    private void setMongoOperations(final MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }
}
