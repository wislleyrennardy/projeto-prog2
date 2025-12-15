package menu;

import model.usuario.Usuario;
import service.BibliotecaService;
import service.PlayerService;
import service.UsuarioService;

import java.util.Scanner;

/**
 * Menu principal do sistema AudioStreaming.
 * 
 * <p>
 * Esta classe gerencia a navegação entre as diferentes funcionalidades
 * do sistema após o login do usuário. Coordena todos os submenus e
 * telas do sistema.
 * </p>
 * 
 * <h2>Funcionalidades Disponíveis</h2>
 * <ol>
 * <li>Catálogo Completo - Lista todas as músicas/podcasts</li>
 * <li>Recomendações - Top Charts por curtidas</li>
 * <li>Buscar - Pesquisa por título ou artista</li>
 * <li>Minhas Playlists - Gerenciamento de playlists</li>
 * <li>Player - Controles de reprodução</li>
 * <li>Meu Perfil - Curtidas e configurações</li>
 * </ol>
 * 
 * @see MenuPlayer
 * @see MenuPlaylist
 * @see TelaCatalogo
 * @see TelaPerfil
 */
public class MenuPrincipal {

    /** Scanner para leitura de entrada do usuário. */
    private final Scanner scanner;

    /** Usuário atualmente logado. */
    private final Usuario usuario;

    /** Serviço de catálogo de áudios. */
    private final BibliotecaService biblioteca;

    /** Serviço de gerenciamento de usuários. */
    private final UsuarioService usuarioService;

    /** Serviço de controle de reprodução. */
    private final PlayerService player;

    /** Submenu do player de reprodução. */
    private final MenuPlayer menuPlayer;

    /** Submenu de gerenciamento de playlists. */
    private final MenuPlaylist menuPlaylist;

    /** Tela de perfil do usuário. */
    private final TelaPerfil telaPerfil;

    /** Tela de catálogo e busca. */
    private final TelaCatalogo telaCatalogo;

    /**
     * Cria o menu principal com todas as dependências.
     * 
     * @param scanner        Scanner para entrada do usuário
     * @param usuario        Usuário logado
     * @param biblioteca     Serviço de catálogo
     * @param usuarioService Serviço de usuários
     * @param player         Serviço de reprodução
     */
    public MenuPrincipal(Scanner scanner, Usuario usuario, BibliotecaService biblioteca,
            UsuarioService usuarioService, PlayerService player) {
        this.scanner = scanner;
        this.usuario = usuario;
        this.biblioteca = biblioteca;
        this.usuarioService = usuarioService;
        this.player = player;

        // Inicializa submenus com as dependências necessárias
        this.menuPlayer = new MenuPlayer(scanner, player, usuario);
        this.menuPlaylist = new MenuPlaylist(scanner, usuario, biblioteca, player, menuPlayer, usuarioService);
        this.telaPerfil = new TelaPerfil(usuario, usuarioService, scanner);
        this.telaCatalogo = new TelaCatalogo(scanner, biblioteca, player, menuPlayer);
        this.telaCatalogo.setUsuario(usuario, usuarioService);
    }

    /**
     * Executa o loop principal do menu.
     */
    public void executarLoop() {
        boolean rodando = true;
        while (rodando) {
            exibirMenu();
            String opcao = scanner.nextLine();

            UtilConsole.limparConsole();

            switch (opcao) {
                case "1" -> telaCatalogo.listarCatalogo();
                case "2" -> telaCatalogo.listarRecomendacoes();
                case "3" -> telaCatalogo.buscarAudio();
                case "4" -> {
                    menuPlaylist.exibir();
                    UtilConsole.limparConsole();
                }
                case "5" -> {
                    menuPlayer.exibir();
                    UtilConsole.limparConsole();
                }
                case "6" -> {
                    if (!telaPerfil.exibir()) {
                        // Conta excluída, encerrar
                        rodando = false;
                    }
                }
                case "0" -> {
                    salvarEEncerrar();
                    rodando = false;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void exibirMenu() {
        System.out.printf("""

                ═══════════════════════════════════════════
                      AUDIOSTREAMING - MENU PRINCIPAL
                  Logado como: %s
                ═══════════════════════════════════════════
                  1. 🎵 Catálogo Completo
                  2. 📊 Recomendações (Top Charts)
                  3. 🔍 Buscar Música/Artista
                  4. 📂 Minhas Playlists
                  5. ⏯️ Player (Controles)
                  6. ❤️ Meu Perfil (Curtidas)
                  0. 💾 Sair e Salvar
                ═══════════════════════════════════════════
                Escolha uma opção:\s""",
                usuario.getNome());
    }

    private void salvarEEncerrar() {
        // Salvar estado do player no usuário
        usuario.setEstadoPlayerIndice(player.getIndiceAtual());

        biblioteca.salvarDadosNoDisco();
        usuarioService.salvarUsuarios();
        System.out.println("Até mais, " + usuario.getNome() + "! 👋");
    }
}
