package floripa.autenticacao.backend.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import floripa.autenticacao.backend.persistence.model.Address;

public interface AddressRepository extends MongoRepository<Address, String>{

}
