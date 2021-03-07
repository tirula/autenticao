package floripa.autenticacao.backend.persistence.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import floripa.autenticacao.backend.persistence.model.User;

/**
 * 
 * @author brunno
 *
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

	Page<User> findAll(Pageable pageable);

	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
}
