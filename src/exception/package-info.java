/**
 * Pacote de exceções customizadas do AudioStreaming.
 * 
 * <p>
 * Este pacote contém as exceções específicas da aplicação,
 * permitindo tratamento de erros personalizado e mensagens
 * mais claras para o usuário.
 * </p>
 * 
 * <h2>Hierarquia de Exceções</h2>
 * 
 * <pre>
 *   RuntimeException
 *     └── BaseAppException
 *           ├── ValidacaoException
 *           └── UsuarioNaoEncontradoException
 * </pre>
 * 
 * <h2>Exceções Disponíveis</h2>
 * <ul>
 * <li>{@link exception.BaseAppException} - Exceção base do sistema</li>
 * <li>{@link exception.ValidacaoException} - Erros de validação de dados</li>
 * <li>{@link exception.UsuarioNaoEncontradoException} - Usuário não existe</li>
 * </ul>
 * 
 * <h2>Por que RuntimeException?</h2>
 * <p>
 * Nossas exceções estendem RuntimeException (unchecked) para evitar
 * a necessidade de try-catch em cada chamada. O tratamento é feito
 * em pontos centrais do código.
 * </p>
 * 
 */
package exception;
