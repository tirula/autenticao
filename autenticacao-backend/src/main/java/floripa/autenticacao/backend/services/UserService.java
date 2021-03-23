package floripa.autenticacao.backend.services;

import java.util.HashSet;
import java.util.Set;

import floripa.autenticacao.backend.payload.request.UsuarioRequest;
import floripa.autenticacao.backend.exception.AutenticationApiException;
import floripa.autenticacao.persistence.model.Address;
import floripa.autenticacao.persistence.model.ERole;
import floripa.autenticacao.persistence.model.Role;
import floripa.autenticacao.persistence.model.User;
import floripa.autenticacao.persistence.repository.AddressRepository;
import floripa.autenticacao.persistence.repository.RoleRepository;
import floripa.autenticacao.persistence.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author brunno
 *
 */
@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder encoder;



	public User buscar(String id){
		User u = this.userRepository.findById(id).orElseThrow(() -> new AutenticationApiException("Usuário não encontrado : " + id));
		return u;
	}
	public User save(UsuarioRequest request) {
		logger.info("save");
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new AutenticationApiException("Usuário já existe");
		}
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new AutenticationApiException("Email já existe cadastrado");
		}
		User user = new User(request.getUsername(), request.getEmail(), encoder.encode(request.getPassword()));
		Set<String> strRoles = request.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Perfil não encontrado."));
			roles.add(userRole);
		} else {
			trataRoles(strRoles, roles);
		}
		user.setRoles(roles);
		if (request.getAddress() != null || request.getPhoneNumber() != null) {
			Address address = new Address();
			address.setPhoneNumber(request.getPhoneNumber());
			address.setStreet(request.getAddress());
			user.setAddress(address);
		}
		return userRepository.save(user);

	}

	private void trataRoles(Set<String> strRoles, Set<Role> roles) {
		strRoles.forEach(role -> {
			switch (role) {
			case "admin":
				Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Perfil não encontrado."));
				roles.add(adminRole);
				break;
			default:
				Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Perfil não encontrado."));
				roles.add(userRole);
			}
		});
	}

	public Page<User> findAll(User u,Pageable pageable) {
		Page<User> users = this.userRepository.findAll(pageable);
		logger.info("Total de registros {} paginados {} ", users.getTotalElements(),users.getContent().size());
		return users;
	}

	public void update(UsuarioRequest request, String id) {
		User u = this.userRepository.findById(id).orElseThrow(() -> new AutenticationApiException("Usuário não encontrado : " + id));
		updatePassword(request, u);
		updateRoles(request, u);
		updateAddress(request, u);
		updatePhoneNumber(request, u);
		this.userRepository.save(u);

	}

	private void updatePhoneNumber(UsuarioRequest request, User u) {
		if (request.getPhoneNumber() != null && u.getAddress() != null) {
			u.getAddress().setPhoneNumber(request.getPhoneNumber());
		} else if (request.getPhoneNumber() != null && u.getAddress() != null) {
			Address add = new Address();
			add.setPhoneNumber(request.getPhoneNumber());
			u.setAddress(add);
		}
	}

	private void updateAddress(UsuarioRequest request, User u) {
		if (request.getAddress() != null && u.getAddress() != null ) {
			u.getAddress().setStreet(request.getAddress());
		} else if (request.getAddress() != null && u.getAddress() == null ) {
			Address add = new Address();
			add.setStreet(request.getAddress());
			u.setAddress(add);
		}
	}

	private void updatePassword(UsuarioRequest request, User u) {
		if (request.getPassword() != null && !request.getPassword().equals("")) {
			u.setPassword(encoder.encode(request.getPassword()));
		}
	}

	private void updateRoles(UsuarioRequest request, User u) {
		if (request.getRoles() != null && !request.getRoles().isEmpty()) {
			Set<String> strRoles = request.getRoles();
			Set<Role> roles = new HashSet<>();
			trataRoles(strRoles, roles);
			u.getRoles().addAll(roles);
		}
	}

	public void delete(String id) {
		User u = buscar(id);
		this.userRepository.delete(u);
	}

}
