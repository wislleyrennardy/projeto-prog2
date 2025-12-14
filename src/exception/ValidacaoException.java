package exception;

/**
 * Exceção lançada quando há erro de validação em operações.
 * 
 * <p>
 * Exemplos: senha muito curta, email inválido, campos vazios.
 * </p>
 * 
 * @see BaseAppException
 */
public class ValidacaoException extends BaseAppException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria exceção de validação.
     * 
     * @param mensagem Descrição do erro de validação
     */
    public ValidacaoException(String mensagem) {
        super(mensagem);
    }
}
