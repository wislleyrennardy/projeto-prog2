/**
 * Pacote de interface com o usuário (menus) do AudioStreaming.
 * 
 * <p>
 * Este pacote contém as classes responsáveis pela interação
 * com o usuário através do console. Implementa uma interface
 * baseada em texto com menus navegáveis.
 * </p>
 * 
 * <h2>Classes Disponíveis</h2>
 * <ul>
 * <li>{@link menu.MenuPrincipal} - Menu principal após login</li>
 * <li>{@link menu.MenuPlayer} - Controles de reprodução</li>
 * <li>{@link menu.MenuPlaylist} - Gerenciamento de playlists</li>
 * <li>{@link menu.TelaCatalogo} - Listagem e busca no catálogo</li>
 * <li>{@link menu.TelaPerfil} - Configurações do usuário</li>
 * <li>{@link menu.TelaAutenticacao} - Login e cadastro</li>
 * <li>{@link menu.UtilConsole} - Utilitários de console</li>
 * </ul>
 * 
 * <h2>Arquitetura</h2>
 * <p>
 * As classes deste pacote seguem o padrão MVC (Model-View-Controller),
 * atuando como a camada View/Controller que:
 * </p>
 * <ul>
 * <li>Exibe informações ao usuário (View)</li>
 * <li>Captura entrada do usuário</li>
 * <li>Chama os serviços apropriados (Controller)</li>
 * </ul>
 * 
 */
package menu;
