package menu;

import model.midia.Audio;
import model.playlist.Playlist;
import model.usuario.Usuario;
import service.BibliotecaService;
import service.PlayerService;
import service.UsuarioService;

import java.util.List;
import java.util.Scanner;

/**
 * Menu de gerenciamento de playlists do usuário.
 * 
 * <p>
 * Esta classe fornece uma interface para criar, visualizar, modificar
 * e excluir playlists. Também permite reproduzir playlists inteiras
 * ou adicionar seus itens à fila de reprodução.
 * </p>
 * 
 * <h2>Funcionalidades</h2>
 * <ul>
 * <li>Listar playlists do usuário</li>
 * <li>Criar novas playlists</li>
 * <li>Visualizar conteúdo de uma playlist</li>
 * <li>Adicionar/remover itens de playlists</li>
 * <li>Tocar playlist ou adicionar à fila</li>
 * <li>Excluir playlists</li>
 * </ul>
 * 
 * @see Playlist
 * @see Usuario
 */
public class MenuPlaylist {

    /** Scanner para leitura de entrada do usuário. */
    private final Scanner scanner;

    /** Usuário logado (dono das playlists). */
    private final Usuario usuario;

    /** Serviço de catálogo para buscar áudios. */
    private final BibliotecaService biblioteca;

    /** Serviço de reprodução. */
    private final PlayerService player;

    /** Submenu do player para navegação após tocar. */
    private final MenuPlayer menuPlayer;

    /** Serviço de usuários para persistência. */
    private final UsuarioService usuarioService;

    /**
     * Cria o menu de playlists com todas as dependências.
     * 
     * @param scanner        Scanner para entrada do usuário
     * @param usuario        Usuário logado
     * @param biblioteca     Serviço de catálogo
     * @param player         Serviço de reprodução
     * @param menuPlayer     Submenu do player
     * @param usuarioService Serviço de persistência de usuários
     */
    public MenuPlaylist(Scanner scanner, Usuario usuario, BibliotecaService biblioteca,
            PlayerService player, MenuPlayer menuPlayer, UsuarioService usuarioService) {
        this.scanner = scanner;
        this.usuario = usuario;
        this.biblioteca = biblioteca;
        this.player = player;
        this.menuPlayer = menuPlayer;
        this.usuarioService = usuarioService;
    }

    /**
     * Exibe o menu principal de playlists.
     */
    public void exibir() {
        UtilConsole.limparConsole();
        boolean noMenu = true;

        while (noMenu) {
            List<Playlist> lists = usuario.getPlaylists();

            System.out.printf("""

                    ═══════════════════════════════════════════
                            📁 MINHAS PLAYLISTS
                    ═══════════════════════════════════════════
                    %s═══════════════════════════════════════════
                      [N] Nova Playlist
                      [0] Voltar ao Menu Principal
                    ═══════════════════════════════════════════
                    Selecione uma playlist ou opção:\s""",
                    formatarListaPlaylists(lists));

            String input = scanner.nextLine().toUpperCase();
            UtilConsole.limparConsole();

            if (input.equals("0")) {
                noMenu = false;
            } else if (input.equals("N")) {
                criarNovaPlaylist();
            } else {
                try {
                    int idx = Integer.parseInt(input) - 1;
                    if (idx >= 0 && idx < lists.size()) {
                        if (!exibirDetalhesPlaylist(lists.get(idx))) {
                            noMenu = false;
                        }
                    } else {
                        System.out.println("Índice inválido.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Opção inválida.");
                }
            }
        }
    }

    private String formatarListaPlaylists(List<Playlist> lists) {
        if (lists.isEmpty()) {
            return "  (Nenhuma playlist criada)\n";
        }
        String resultado = "";
        for (int i = 0; i < lists.size(); i++) {
            Playlist p = lists.get(i);
            resultado = resultado + "  " + (i + 1) + ". " + p.getNome() + " (" + p.getItens().size() + " itens)\n";
        }
        return resultado;
    }

    private void criarNovaPlaylist() {
        System.out.print("Nome da nova playlist: ");
        String nome = scanner.nextLine().trim();
        if (!nome.isEmpty()) {
            usuario.criarPlaylist(nome);
            usuarioService.salvarUsuarios();
            System.out.println("✔ Playlist '" + nome + "' criada com sucesso.");
        } else {
            System.out.println("Nome não pode ser vazio.");
        }
    }

    /**
     * Exibe os detalhes de uma playlist com todas as opções disponíveis.
     * 
     * @return true para continuar no menu de playlists, false para sair
     */
    private boolean exibirDetalhesPlaylist(Playlist playlist) {
        boolean noDetalhe = true;

        while (noDetalhe) {
            List<Audio> itens = playlist.getItens();

            System.out.printf("""

                    ═══════════════════════════════════════════
                            📁 %s
                    ═══════════════════════════════════════════
                    %s═══════════════════════════════════════════
                      [T] Tocar playlist (substitui fila)
                      [F] Adicionar à fila de reprodução
                      [A] Adicionar item
                      [R] Remover item
                      [X] Excluir playlist
                      [V] Voltar para lista de playlists
                      [0] Voltar ao Menu Principal
                    ═══════════════════════════════════════════
                    >>\s""",
                    playlist.getNome(),
                    formatarItensPlaylist(itens));

            String cmd = scanner.nextLine().toUpperCase();
            UtilConsole.limparConsole();

            switch (cmd) {
                case "T" -> {
                    if (itens.isEmpty()) {
                        System.out.println("Playlist vazia. Adicione itens primeiro.");
                    } else {
                        if (player.definirFila(itens)) {
                            System.out.println("Fila de reprodução atualizada com " + itens.size() + " itens.");
                            player.play();
                            Audio atual = player.getAudioAtual();
                            if (atual != null) {
                                System.out.println("▶ Tocando: " + atual.getDetalhesFormatados());
                            }
                        }
                        menuPlayer.exibir();
                        UtilConsole.limparConsole();
                    }
                }
                case "F" -> {
                    if (itens.isEmpty()) {
                        System.out.println("Playlist vazia.");
                    } else {
                        int qtd = player.adicionarListaAFila(itens, playlist.getNome());
                        if (qtd > 0) {
                            System.out.println("➕ '" + playlist.getNome() + "' adicionada à fila (" + qtd
                                    + " itens, total: " + player.getTamanhoFila() + ")");
                        }
                    }
                }
                case "A" -> adicionarItemPlaylist(playlist);
                case "R" -> removerItemPlaylist(playlist);
                case "X" -> {
                    if (confirmarExclusaoPlaylist(playlist))
                        return true;
                }
                case "V" -> noDetalhe = false;
                case "0" -> {
                    return false;
                }
                default -> {
                }
            }
        }
        return true;
    }

    private String formatarItensPlaylist(List<Audio> itens) {
        if (itens.isEmpty()) {
            return "  (Playlist vazia)\n";
        }
        String resultado = "";
        for (int i = 0; i < itens.size(); i++) {
            Audio audio = itens.get(i);
            resultado = resultado + "  " + (i + 1) + ". ♪ " + audio.getDetalhesFormatados() + "\n";
        }
        return resultado;
    }

    private void adicionarItemPlaylist(Playlist playlist) {
        List<Audio> audios = biblioteca.getCatalogo();

        if (audios.isEmpty()) {
            System.out.println("Catálogo vazio.");
            return;
        }

        final int ITENS_POR_PAGINA = 20;
        int totalPaginas = (int) Math.ceil((double) audios.size() / ITENS_POR_PAGINA);
        int paginaAtual = 0;

        boolean navegando = true;
        while (navegando) {
            UtilConsole.limparConsole();

            int inicio = paginaAtual * ITENS_POR_PAGINA;
            int fim = Math.min(inicio + ITENS_POR_PAGINA, audios.size());
            String infoPagina = "Página " + (paginaAtual + 1) + "/" + totalPaginas + " (" + audios.size() + " itens)";

            System.out.printf("""
                    ═══════════════════════════════════════════
                      ➕ ADICIONAR ITEM À PLAYLIST
                      %s
                    ═══════════════════════════════════════════
                    %s═══════════════════════════════════════════
                    %s  [0] Cancelar
                    ═══════════════════════════════════════════
                    Digite o número do item:\s""",
                    infoPagina,
                    formatarListaAudios(audios, inicio, fim),
                    formatarNavegacaoPagina(paginaAtual, totalPaginas));

            String input = scanner.nextLine().toUpperCase();
            UtilConsole.limparConsole();

            switch (input) {
                case "P" -> {
                    if (paginaAtual < totalPaginas - 1)
                        paginaAtual++;
                }
                case "A" -> {
                    if (paginaAtual > 0)
                        paginaAtual--;
                }
                case "0" -> navegando = false;
                default -> {
                    try {
                        int idx = Integer.parseInt(input) - 1;
                        if (idx >= 0 && idx < audios.size()) {
                            Audio audio = audios.get(idx);
                            if (playlist.adicionarItem(audio)) {
                                usuarioService.salvarUsuarios();
                                System.out
                                        .println("✓ '" + audio.getTitulo() + "' adicionado à '" + playlist.getNome()
                                                + "'");
                            } else {
                                System.out.println("⚠ '" + audio.getTitulo() + "' já existe na playlist.");
                            }
                            navegando = false;
                        } else {
                            System.out.println("Índice inválido.");
                        }
                    } catch (NumberFormatException e) {
                        // Comando não reconhecido
                    }
                }
            }
        }
    }

    private String formatarListaAudios(List<Audio> audios, int inicio, int fim) {
        String resultado = "";
        for (int i = inicio; i < fim; i++) {
            resultado = resultado + "  " + (i + 1) + ". " + audios.get(i) + "\n";
        }
        return resultado;
    }

    private String formatarNavegacaoPagina(int paginaAtual, int totalPaginas) {
        String resultado = "";
        if (paginaAtual > 0) {
            resultado = resultado + "  [A] Página Anterior\n";
        }
        if (paginaAtual < totalPaginas - 1) {
            resultado = resultado + "  [P] Próxima Página\n";
        }
        return resultado;
    }

    private void removerItemPlaylist(Playlist playlist) {
        List<Audio> itens = playlist.getItens();

        if (itens.isEmpty()) {
            System.out.println("Playlist vazia.");
            return;
        }

        System.out.println("\n--- Itens na Playlist ---");
        for (int i = 0; i < itens.size(); i++) {
            System.out.println((i + 1) + ". " + itens.get(i).getDetalhesFormatados());
        }
        System.out.println("0. Cancelar");
        System.out.print("\nSelecione o item para remover: ");

        try {
            String input = scanner.nextLine();
            if (input.equals("0") || input.isEmpty()) {
                return;
            }

            int idx = Integer.parseInt(input) - 1;
            if (idx >= 0 && idx < itens.size()) {
                Audio removido = itens.remove(idx);
                usuarioService.salvarUsuarios();
                System.out.println("✓ '" + removido.getTitulo() + "' removido da playlist.");
            } else {
                System.out.println("Índice inválido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
        }
    }

    private boolean confirmarExclusaoPlaylist(Playlist playlist) {
        System.out.print("Tem certeza que deseja excluir '" + playlist.getNome() + "'? (S/N): ");
        String confirmacao = scanner.nextLine().toUpperCase();

        if (confirmacao.equals("S")) {
            usuario.getPlaylists().remove(playlist);
            usuarioService.salvarUsuarios();
            System.out.println("✓ Playlist '" + playlist.getNome() + "' excluída.");
            return true;
        }
        return false;
    }
}
