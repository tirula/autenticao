package floripa.creative.drive.backend.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import floripa.creative.drive.backend.payload.request.UsuarioRequest;
import floripa.creative.drive.backend.payload.response.MessageResponse;
import floripa.creative.drive.backend.services.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/usuario")
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
	public ResponseEntity<?>  userAccess() {
		return ResponseEntity.ok(this.userService.findAll());
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}
	
	
}
