package floripa.autenticacao.backend.rest;

import floripa.autenticacao.backend.payload.request.UsuarioRequest;
import floripa.autenticacao.backend.persistence.model.User;
import floripa.autenticacao.backend.persistence.repository.UserRepository;
import floripa.autenticacao.backend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
@SpringBootTest
public class UsuarioRestTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService service;

    @BeforeEach
    void setMockOutput() {
        when(userRepository.findById(Mockito.anyString())).thenReturn(creaeteFakeUser());
        when(encoder.encode(Mockito.anyString())).thenReturn("hashsenha");
    }

    private Optional<User> creaeteFakeUser() {
        User u = new User();
        u.setEmail("email@feliz.com");
        return Optional.of(u);
    }

    @Test
    void checkAdminUser() throws Exception {
        UsuarioRequest reques = new UsuarioRequest();
        reques.setUsername("admin");
        reques.setPassword("123");
        reques.setEmail("email@feliz.com.com");
        reques.setPhoneNumber("998103166");
        reques.setAddress("endereco legal");
        this.service.save(reques);
       Optional<User> u = userRepository.findByUsername("admin");
       String a ="";
    }
}
