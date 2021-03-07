package floripa.autenticacao.backend.persistence.repository;

import java.util.Optional;

import floripa.autenticacao.backend.persistence.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import floripa.autenticacao.backend.persistence.model.ERole;

/**
 * 
 * @author brunno
 *
 */
public interface RoleRepository extends MongoRepository<Role, String> {
	Optional<Role> findByName(ERole name);
}
