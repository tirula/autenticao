package floripa.autenticacao.backend.exception;
/**
 * 
 * @author brunno
 *
 */
public class AutenticationApiException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2135312729567928501L;

	public AutenticationApiException(String message){
		super(message);
	}

}
