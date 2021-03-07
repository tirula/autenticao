package floripa.autenticacao.backend.rest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import floripa.autenticacao.backend.persistence.model.User;
import floripa.autenticacao.backend.persistence.repository.UserRepository;
import floripa.autenticacao.backend.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UsuarioRestTest {

    @Autowired
    private MockMvc mockMvc;

    private String adminAccessToken ="";
    private String userAccessToken ="";



    @BeforeEach
    void setMockOutput() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        var jsonMap = new HashMap<String, String>();
        jsonMap.put("username","admin");
        jsonMap.put("password","admin");
        String json = mapper.writeValueAsString(jsonMap);
        ResultActions result  = mockMvc.perform(
                post("/auth")
                        .header("Content-Type","application/json")
                        .content(json))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("admin")))
                .andExpect(jsonPath("$.email", notNullValue()));
        String resultString = result.andReturn().getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        this.adminAccessToken = jsonParser.parseMap(resultString).get("accessToken").toString();
        System.out.println(resultString);
    }


    @Test
    public void consultaUsuarios() throws Exception {
        ResultActions result  = mockMvc.perform(
                get("/usuarios")
                        .header("Content-Type","application/json")
                        .header("Authorization","Bearer " + this.adminAccessToken))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", notNullValue()))
                .andExpect(jsonPath("$.totalItems", notNullValue()))
                .andExpect(jsonPath("$.totalPages", notNullValue()))
                .andExpect(jsonPath("$.totalItems", notNullValue()))
                .andExpect(jsonPath("$.currentPage", notNullValue()));
        String resultString = result.andReturn().getResponse().getContentAsString();
    }
    @Test
    public void consultaUsuariosNaoAutorizado() throws Exception {
        ResultActions result  = mockMvc.perform(
                get("/usuarios")
                        .header("Content-Type","application/json"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void criarDeletarUsuario() throws Exception {
        String json = criarJsonUsuario();
        ResultActions result = inserirUsuario(json);
        String resultString = result.andReturn().getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String id = jsonParser.parseMap(resultString).get("id").toString();
        deletarUsuario(id);
    }


    @Test
    public void criarEditarDeletarUsuario() throws Exception {
        String json = criarJsonUsuario();
        ResultActions result = inserirUsuario(json);
        String resultString = result.andReturn().getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String id = jsonParser.parseMap(resultString).get("id").toString();
        editarUsuario(id);
        deletarUsuario(id);
    }

    private String criarJsonUsuario() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        var jsonMap = new HashMap<String, Object>();
        jsonMap.put("username","usuario_feliz");
        jsonMap.put("password","senhaFelix");
        jsonMap.put("email","email@feliz.com.br");
        jsonMap.put("address","rua feliz cidade feliz");
        jsonMap.put("roles", List.of("user"));
        return mapper.writeValueAsString(jsonMap);
    }

    private ResultActions inserirUsuario(String json) throws Exception {
        return mockMvc.perform(
                    post("/usuarios")
                            .header("Content-Type","application/json")
                            .header("Authorization","Bearer " + this.adminAccessToken)
                            .content(json))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk());
    }

    private void deletarUsuario(String id) throws Exception {
        mockMvc.perform(
                delete("/usuarios/"+id)
                        .header("Authorization","Bearer " + this.adminAccessToken)
                        .header("Content-Type","application/json"))
                .andExpect(status().isOk());
    }

    private void editarUsuario(String id) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        var jsonMap = new HashMap<String, Object>();
        String novaRua = "Rua meio feliz";
        jsonMap.put("address",novaRua);
        String json = mapper.writeValueAsString(jsonMap);
        mockMvc.perform(
                put("/usuarios/"+id)
                        .header("Authorization","Bearer " + this.adminAccessToken)
                        .header("Content-Type","application/json")
                        .content(json))
                .andExpect(status().isOk());


        mockMvc.perform(
                get("/usuarios/"+id)
                        .header("Authorization","Bearer " + this.adminAccessToken)
                        .header("Content-Type","application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.username", notNullValue()))
                .andExpect(jsonPath("$.email", notNullValue()))
                .andExpect(jsonPath("$.roles", notNullValue()))
                .andExpect(jsonPath("$.address", notNullValue()))
                .andExpect(jsonPath("$.address.street", is(novaRua)));
    }



}
