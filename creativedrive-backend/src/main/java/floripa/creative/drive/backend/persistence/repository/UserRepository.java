package floripa.creative.drive.backend.persistence.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import floripa.creative.drive.backend.persistence.model.User;

/**
 * 
 * @author brunno
 *
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
}
