/**
 * Pacote de modelos de usuário do AudioStreaming.
 * 
 * <p>
 * Este pacote contém as classes relacionadas à representação
 * e gerenciamento de usuários do sistema.
 * </p>
 * 
 * <h2>Classes Disponíveis</h2>
 * <ul>
 * <li>{@link model.usuario.Usuario} - Representa um usuário do sistema</li>
 * </ul>
 * 
 * <h2>Funcionalidades do Usuário</h2>
 * <ul>
 * <li>Autenticação com senha hash (SHA-256)</li>
 * <li>Playlists personalizadas (List&lt;Playlist&gt;)</li>
 * <li>Sistema de curtidas (Set&lt;Audio&gt;)</li>
 * <li>Persistência do estado do player</li>
 * </ul>
 * 
 * <h2>Uso de Coleções</h2>
 * <ul>
 * <li>List&lt;Playlist&gt; - Lista ordenada de playlists</li>
 * <li>Set&lt;Audio&gt; - Conjunto de curtidas (sem duplicatas)</li>
 * </ul>
 * 
 */
package model.usuario;