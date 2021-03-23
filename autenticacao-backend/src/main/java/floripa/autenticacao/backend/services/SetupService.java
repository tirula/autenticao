package floripa.autenticacao.backend.services;

import floripa.autenticacao.backend.payload.request.UsuarioRequest;
import floripa.autenticacao.persistence.model.ERole;
import floripa.autenticacao.persistence.model.Role;
import floripa.autenticacao.persistence.model.User;
import floripa.autenticacao.persistence.repository.RoleRepository;
import floripa.autenticacao.persistence.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

/**
 *
 * @author brunno
 *
 */
@Service
public class SetupService {

    private static final Logger logger = LoggerFactory.getLogger(SetupService.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @EventListener
    public void firstTry(ApplicationReadyEvent event) {
        logger.info("Verificando inserção de dados básicos");
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
            this.userService.save(request);
        }
    }
}
