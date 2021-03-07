package floripa.autenticacao.backend.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import floripa.autenticacao.backend.exception.CreativeDriveException;
import floripa.autenticacao.backend.persistence.model.Address;
import floripa.autenticacao.backend.persistence.model.ERole;
import floripa.autenticacao.backend.persistence.model.Role;
import floripa.autenticacao.backend.persistence.model.User;
import floripa.autenticacao.backend.persistence.repository.AddressRepository;
import floripa.autenticacao.backend.persistence.repository.RoleRepository;
import floripa.autenticacao.backend.persistence.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import floripa.autenticacao.backend.payload.request.UsuarioRequest;

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
	private AddressRepository addressRepository;

	@Autowired
	private PasswordEncoder encoder;

	@EventListener
	public void firstTry(ApplicationReadyEvent event) {
		logger.info("Verificando insercao de dados basicos");
		Optional<Role> role = this.roleRepository.findByName(ERole.ROLE_ADMIN);
		if (role.isEmpty()) {
			Role r = new Role();
			r.setName(ERole.ROLE_ADMIN);
			this.roleRepository.save(r);
		}
		role = this.roleRepository.findByName(ERole.ROLE_USER);
		if (role.isEmpty()) {
			Role r = new Role();
			r.setName(ERole.ROLE_USER);
			this.roleRepository.save(r);
		}
		Optional<User> admin = this.userRepository.findByUsername("admin");
		if (admin.isEmpty()) {
			UsuarioRequest request = new UsuarioRequest();
			request.setEmail("admin@admin.com");
			request.setPassword("admin");
			request.setUsername("admin");
			request.setRole(Set.of("admin"));
			save(request);
		}

		// repo.save(new Entity(...));
	}

	public void save(UsuarioRequest request) {
		logger.info("save");
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new CreativeDriveException("Usuario ja existe");
		}
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new CreativeDriveException("Email ja existe cadastrado");
		}
		User user = new User(request.getUsername(), request.getEmail(), encoder.encode(request.getPassword()));
		Set<String> strRoles = request.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Perfil nao encontrado."));
			roles.add(userRole);
		} else {
			trataRoles(strRoles, roles);
		}
		user.setRoles(roles);
		if (request.getAddress() != null || request.getPhoneNumber() != null) {
			Address address = new Address();
			address.setPhoneNumber(request.getPhoneNumber());
			address.setStreet(request.getAddress());
			this.addressRepository.save(address);
			user.setAddress(address);
		}
		userRepository.save(user);

	}

	private void trataRoles(Set<String> strRoles, Set<Role> roles) {
		strRoles.forEach(role -> {
			switch (role) {
			case "admin":
				Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Perfil nao encontrado."));
				roles.add(adminRole);
				break;
			default:
				Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Perfil nao encontrado."));
				roles.add(userRole);
			}
		});
	}

	public Page<User> findAll(User u,Pageable pageable) {
//		ExampleMatcher matcher = ExampleMatcher
//				.matchingAny()
//				.withMatcher("username", match -> match.ignoreCase())
//				//.withMatcher("email", match -> match.ignoreCase())
//				.withIncludeNullValues()
//				.withIgnorePaths("id","password","address","roles")
//				;
//		Example<User> userExample = Example.of(u,matcher);
		Page<User> users = this.userRepository.findAll(pageable);
		logger.info("Total de registros {} paginados {} ", users.getTotalElements(),users.getContent().size());
		return users;
	}

	public void update(UsuarioRequest request, String id) {
		User u = this.userRepository.findById(id).orElseThrow(() -> new CreativeDriveException("Usuario nao encontrado : " + id));
		if (request.getPassword() != null && request.getPassword().equals("")) {
			u.setPassword(encoder.encode(request.getPassword()));
		}
		if (request.getRoles() != null && !request.getRoles().isEmpty()) {
			Set<String> strRoles = request.getRoles();
			Set<Role> roles = new HashSet<>();
			trataRoles(strRoles, roles);
			u.setRoles(roles);
		}

		if (request.getAddress() != null && u.getAddress() != null ) {
			u.getAddress().setStreet(request.getAddress());
		} else if (request.getAddress() != null && u.getAddress() == null ) {
			Address add = new Address();
			add.setStreet(request.getAddress());
			this.addressRepository.save(add);
			u.setAddress(add);
		}

		if (request.getPhoneNumber() != null && u.getAddress() != null) {
			u.getAddress().setPhoneNumber(request.getPhoneNumber());
		} else if (request.getPhoneNumber() != null && u.getAddress() != null) {
			Address add = new Address();
			add.setPhoneNumber(request.getPhoneNumber());
			this.addressRepository.save(add);
			u.setAddress(add);
		}
		this.userRepository.save(u);

	}

	public void delete(String id) {
		User u = this.userRepository.findById(id).orElseThrow(() -> new CreativeDriveException("Usuario nao encontrado : " + id));
		this.userRepository.delete(u);
	}

}
