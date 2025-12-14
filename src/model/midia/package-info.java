/**
 * Pacote de modelos de mídia do AudioStreaming.
 * 
 * <p>
 * Este pacote contém as classes que representam os diferentes
 * tipos de conteúdo de áudio disponíveis no sistema.
 * </p>
 * 
 * <h2>Hierarquia de Classes</h2>
 * 
 * <pre>
 *   Audio (abstrata)
 *     ├── Musica
 *     └── Podcast
 *   
 *   Artista
 *   Album
 * </pre>
 * 
 * <h2>Classes Disponíveis</h2>
 * <ul>
 * <li>{@link model.midia.Audio} - Classe base abstrata para todo conteúdo</li>
 * <li>{@link model.midia.Musica} - Representa uma música com artista e
 * álbum</li>
 * <li>{@link model.midia.Podcast} - Representa um episódio de podcast</li>
 * <li>{@link model.midia.Artista} - Representa um artista musical</li>
 * <li>{@link model.midia.Album} - Representa um álbum musical</li>
 * </ul>
 * 
 * <h2>Conceitos de POO Aplicados</h2>
 * <ul>
 * <li><b>Herança:</b> Musica e Podcast estendem Audio</li>
 * <li><b>Encapsulamento:</b> Atributos privados com getters/setters</li>
 * <li><b>Polimorfismo:</b> Métodos reproduzir() diferentes em cada
 * subclasse</li>
 * </ul>
 * 
 */
package model.midia;