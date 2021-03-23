package floripa.autenticacao.persistence.repository;

import floripa.autenticacao.persistence.model.Address;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AddressRepository extends MongoRepository<Address, String>{

}
