package floripa.autenticacao.backend.exception.handle;

import java.io.IOException;
import java.time.LocalDateTime;

import floripa.autenticacao.backend.exception.AutenticationApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * @author brunno
 *
 */
@ControllerAdvice
@ApiIgnore
public class CustomGlobalExceptionHandler {


	@ExceptionHandler(AutenticationApiException.class)
	public ResponseEntity<CustomErrorResponse> commonsBadRequest(Exception ex, WebRequest request) throws IOException {
		CustomErrorResponse errors = new CustomErrorResponse();
		errors.setTimestamp(LocalDateTime.now());
		errors.setError(ex.getMessage());
		errors.setStatus(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
	

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<CustomErrorResponse> accessDenied(Exception ex, WebRequest request) throws IOException {
		CustomErrorResponse errors = new CustomErrorResponse();
		errors.setTimestamp(LocalDateTime.now());
		errors.setError(ex.getMessage());
		errors.setStatus(HttpStatus.FORBIDDEN.value());
		return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
	}
}