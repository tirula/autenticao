package floripa.autenticacao.persistence.repository;

import java.util.Optional;

import floripa.autenticacao.persistence.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import floripa.autenticacao.persistence.model.ERole;

/**
 * 
 * @author brunno
 *
 */
public interface RoleRepository extends MongoRepository<Role, String> {
	Optional<Role> findByName(ERole name);
}
