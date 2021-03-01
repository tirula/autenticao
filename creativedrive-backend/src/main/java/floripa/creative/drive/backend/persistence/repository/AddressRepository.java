package floripa.creative.drive.backend.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import floripa.creative.drive.backend.persistence.model.Address;

public interface AddressRepository extends MongoRepository<Address, String>{

}
