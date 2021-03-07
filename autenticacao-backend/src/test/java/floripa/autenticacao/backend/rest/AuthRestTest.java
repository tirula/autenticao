package floripa.autenticacao.backend.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import floripa.autenticacao.backend.persistence.model.User;
import floripa.autenticacao.backend.security.jwt.JwtUtils;
import floripa.autenticacao.backend.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthRestTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setMockOutput() {

    }

    @Test
    public void autenticaAdminTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        var jsonMap = new HashMap<String, String>();
        jsonMap.put("username","admin");
        jsonMap.put("password","admin");
        String json = mapper.writeValueAsString(jsonMap);
        mockMvc.perform(
                    post("/auth")
                    .header("Content-Type","application/json")
                    .content(json))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("admin")))
                .andExpect(jsonPath("$.email", notNullValue()))
                .andExpect(jsonPath("$.roles", notNullValue()))
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andExpect(jsonPath("$.tokenType", is("Bearer")));

        //verify(authenticationManager, times(1)).authenticate(any());
    }
}
