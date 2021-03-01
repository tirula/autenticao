package floripa.creative.drive.backend.persistence.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import floripa.creative.drive.backend.persistence.model.ERole;
import floripa.creative.drive.backend.persistence.model.Role;

/**
 * 
 * @author brunno
 *
 */
public interface RoleRepository extends MongoRepository<Role, String> {
	Optional<Role> findByName(ERole name);
}
