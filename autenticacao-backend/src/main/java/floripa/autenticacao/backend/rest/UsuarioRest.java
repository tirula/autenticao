package floripa.autenticacao.backend.rest;

import javax.validation.Valid;

import floripa.autenticacao.backend.payload.request.UsuarioRequest;
import floripa.autenticacao.backend.payload.response.MessageResponse;
import floripa.autenticacao.backend.persistence.model.User;
import floripa.autenticacao.backend.services.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/usuarios")
@Api(tags={"Usuários" },description = "")
public class UsuarioRest {
	
	@Autowired
	private UserService userService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(consumes = "application/json", produces  = "application/json")
	public ResponseEntity<?> novoUsuario(@Valid @RequestBody UsuarioRequest request) {
		this.userService.save(request);
		return ResponseEntity.ok(new MessageResponse("Usuario salvo!"));
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(path = "{id}",consumes = "application/json", produces  = "application/json")
	public ResponseEntity<?> editarUsuario(
			@RequestBody UsuarioRequest request,
			@PathVariable("id")String id) {
		this.userService.update(request,id);
		return null;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(path = "{id}",consumes = "application/json", produces  = "application/json")
	public ResponseEntity<?> deletarUsuario(@PathVariable("id")String id) {
		this.userService.delete(id);
		return ResponseEntity.ok("Usuario excluido com sucesso");
	}
	
	
	@GetMapping(consumes = "application/json", produces  = "application/json")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<?>  buscarUsuarios(
			@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "email", required = false) String email
			) {
		Pageable pageable = PageRequest.of(offset, limit);
		User u = new User();
		u.setEmail(email);
		Page<User> users = this.userService.findAll(u,pageable);
		Map<String, Object> response = new HashMap<>();
		response.put("data", users.getContent());
		response.put("currentPage", users.getNumber());
		response.put("totalItems", users.getTotalElements());
		response.put("totalPages", users.getTotalPages());
		return ResponseEntity.ok(response);
	}
}
