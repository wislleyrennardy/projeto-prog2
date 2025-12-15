/**
 * Pacote de modelos de playlist do AudioStreaming.
 * 
 * <p>
 * Este pacote contém as classes relacionadas ao gerenciamento
 * de playlists personalizadas dos usuários.
 * </p>
 * 
 * <h2>Classes Disponíveis</h2>
 * <ul>
 * <li>{@link model.playlist.Playlist} - Representa uma playlist do usuário</li>
 * </ul>
 * 
 * <h2>Uso de Coleções</h2>
 * <p>
 * A classe Playlist utiliza List&lt;Audio&gt; para armazenar seus itens
 * porque:
 * </p>
 * <ul>
 * <li>Mantém a ordem de inserção (importante para playlists)</li>
 * <li>Permite acesso por índice</li>
 * <li>Suporta duplicatas se necessário</li>
 * </ul>
 * 
 * @see model.midia.Audio
 */
package model.playlist;