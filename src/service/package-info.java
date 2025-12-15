/**
 * Pacote de serviços (camada de lógica de negócios) do AudioStreaming.
 * 
 * <p>
 * Este pacote contém as classes de serviço que implementam a lógica
 * de negócios da aplicação, separando-a da interface do usuário (menu)
 * e dos modelos de dados (model).
 * </p>
 * 
 * <h2>Serviços Disponíveis</h2>
 * <ul>
 * <li>{@link service.BibliotecaService} - Gerencia catálogo de áudios e
 * busca</li>
 * <li>{@link service.PlayerService} - Controla reprodução (play, pause,
 * fila)</li>
 * <li>{@link service.UsuarioService} - Gerencia autenticação e usuários</li>
 * </ul>
 * 
 * <h2>Padrões de Projeto Utilizados</h2>
 * <ul>
 * <li><b>Singleton:</b> BibliotecaService e UsuarioService usam instância
 * única</li>
 * <li><b>Service Layer:</b> Separa lógica de negócios da apresentação</li>
 * </ul>
 * 
 * <h2>Uso de Coleções</h2>
 * <ul>
 * <li>List&lt;Audio&gt; - Catálogo e fila de reprodução</li>
 * <li>Map&lt;String, List&lt;Audio&gt;&gt; - Índice de busca rápida</li>
 * <li>Map&lt;String, Usuario&gt; - Mapeamento email → usuário</li>
 * </ul>
 * 
 */
package service;