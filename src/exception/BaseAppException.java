package exception;

/**
 * Exceção base para erros específicos do AudioStreaming.
 * 
 * <p>
 * Esta classe serve como base para todas as exceções customizadas
 * do sistema, permitindo tratamento específico de erros da aplicação.
 * </p>
 * 
 * @see Exception
 */
public class BaseAppException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria uma nova exceção com mensagem.
     * 
     * @param mensagem Descrição do erro
     */
    public BaseAppException(String mensagem) {
        super(mensagem);
    }

    /**
     * Cria uma nova exceção com mensagem e causa.
     * 
     * @param mensagem Descrição do erro
     * @param causa    Exceção original que causou este erro
     */
    public BaseAppException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
