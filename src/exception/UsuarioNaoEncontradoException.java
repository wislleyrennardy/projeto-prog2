package exception;

/**
 * Exceção lançada quando um usuário não é encontrado no sistema.
 * 
 * <p>
 * Tipicamente ocorre durante tentativas de login com email
 * não cadastrado.
 * </p>
 * 
 * @see BaseAppException
 */
public class UsuarioNaoEncontradoException extends BaseAppException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria exceção para usuário não encontrado.
     * 
     * @param email Email do usuário não encontrado
     */
    public UsuarioNaoEncontradoException(String email) {
        super("Usuário não encontrado: " + email);
    }
}
