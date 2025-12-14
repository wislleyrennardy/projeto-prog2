package menu;

import model.midia.Audio;
import model.playlist.Playlist;
import model.usuario.Usuario;
import service.BibliotecaService;
import service.PlayerService;
import service.UsuarioService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Tela de catálogo de áudios do sistema.
 * 
 * <p>
 * Esta classe fornece uma interface para navegar pelo catálogo
 * completo de músicas e podcasts. Suporta paginação, busca por
 * termo e visualização de recomendações baseadas em popularidade.
 * </p>
 * 
 * <h2>Funcionalidades</h2>
 * <ul>
 * <li>Listar catálogo completo com paginação</li>
 * <li>Buscar por título ou artista</li>
 * <li>Ver recomendações (Top Charts)</li>
 * <li>Adicionar itens à fila ou playlist</li>
 * <li>Curtir/descurtir itens</li>
 * </ul>
 * 
 * @see BibliotecaService
 * @see Audio
 */
public class TelaCatalogo {

    /** Scanner para leitura de entrada do usuário. */
    private final Scanner scanner;

    /** Serviço de catálogo de áudios. */
    private final BibliotecaService biblioteca;

    /** Serviço de reprodução. */
    private final PlayerService player;

    /** Submenu do player para navegação. */
    private final MenuPlayer menuPlayer;

    /** Usuário logado (para curtidas e playlists). */
    private Usuario usuario;

    /** Serviço de usuários para persistência. */
    private UsuarioService usuarioService;

    /**
     * Cria a tela de catálogo com dependências básicas.
     * 
     * @param scanner    Scanner para entrada do usuário
     * @param biblioteca Serviço de catálogo
     * @param player     Serviço de reprodução
     * @param menuPlayer Submenu do player
     */
    public TelaCatalogo(Scanner scanner, BibliotecaService biblioteca, PlayerService player, MenuPlayer menuPlayer) {
        this.scanner = scanner;
        this.biblioteca = biblioteca;
        this.player = player;
        this.menuPlayer = menuPlayer;
    }

    /**
     * Define o usuário logado para operações que precisam dele.
     * 
     * <p>
     * Chamado após o login para permitir operações de curtida
     * e adição a playlists.
     * </p>
     * 
     * @param usuario        Usuário logado
     * @param usuarioService Serviço de persistência
     */
    public void setUsuario(Usuario usuario, UsuarioService usuarioService) {
        this.usuario = usuario;
        this.usuarioService = usuarioService;
    }

    /**
     * Lista todo o catálogo de áudios com paginação.
     */
    public void listarCatalogo() {
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
                            🎵 CATÁLOGO COMPLETO
                      %s
                    ═══════════════════════════════════════════
                    %s═══════════════════════════════════════════
                    %s  [V] Voltar ao Menu
                    ═══════════════════════════════════════════
                    Selecione um item ou opção:\s""",
                    infoPagina,
                    formatarListaAudios(audios, inicio, fim),
                    formatarNavegacaoPagina(paginaAtual, totalPaginas));

            String cmd = scanner.nextLine().toUpperCase();
            UtilConsole.limparConsole();

            switch (cmd) {
                case "P" -> {
                    if (paginaAtual < totalPaginas - 1)
                        paginaAtual++;
                }
                case "A" -> {
                    if (paginaAtual > 0)
                        paginaAtual--;
                }
                case "V" -> navegando = false;
                default -> {
                    try {
                        int idx = Integer.parseInt(cmd) - 1;
                        if (idx >= 0 && idx < audios.size()) {
                            if (!exibirDetalhesAudio(audios.get(idx))) {
                                navegando = false;
                            }
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

    /**
     * Busca áudios por termo com navegação interativa nos resultados.
     */
    public void buscarAudio() {
        System.out.print("""
                ═══════════════════════════════════════════
                      🔍 BUSCAR MÚSICA/ARTISTA
                ═══════════════════════════════════════════
                Digite o termo de busca:\s""");

        String termo = scanner.nextLine().trim();

        if (termo.isEmpty()) {
            System.out.println("Termo de busca vazio.");
            return;
        }

        UtilConsole.limparConsole();

        List<Audio> resultados = biblioteca.buscar(termo);

        if (resultados.isEmpty()) {
            System.out.println("Nenhum resultado encontrado para: '" + termo + "'");
            return;
        }

        boolean navegando = true;
        while (navegando) {
            System.out.printf("""

                    ═══════════════════════════════════════════
                      🔍 Resultados para: %s
                      %s
                    ═══════════════════════════════════════════
                    %s═══════════════════════════════════════════
                      [T] Tocar todos os resultados
                      [V] Voltar ao Menu
                    ═══════════════════════════════════════════
                    Selecione um item ou opção:\s""",
                    termo,
                    resultados.size() + " item(s) encontrado(s)",
                    formatarListaAudios(resultados, 0, resultados.size()));

            String cmd = scanner.nextLine().toUpperCase();
            UtilConsole.limparConsole();

            switch (cmd) {
                case "T" -> {
                    if (player.definirFila(resultados)) {
                        System.out.println("Fila de reprodução atualizada com " + resultados.size() + " itens.");
                        player.play();
                        Audio a = player.getAudioAtual();
                        if (a != null) {
                            System.out.println("▶ Tocando: " + a.getDetalhesFormatados());
                        }
                    }
                    menuPlayer.exibir();
                    UtilConsole.limparConsole();
                }
                case "V" -> navegando = false;
                default -> {
                    try {
                        int idx = Integer.parseInt(cmd) - 1;
                        if (idx >= 0 && idx < resultados.size()) {
                            if (!exibirDetalhesAudio(resultados.get(idx))) {
                                navegando = false;
                            }
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

    /**
     * Lista recomendações baseadas em curtidas com navegação interativa.
     */
    public void listarRecomendacoes() {
        UtilConsole.limparConsole();
        boolean noMenu = true;

        while (noMenu) {
            List<Audio> recs = biblioteca.recomendarMaisCurtidos();

            System.out.printf("""

                    ═══════════════════════════════════════════
                            🔥 TOP CHARTS
                        (Músicas Mais Curtidas)
                    ═══════════════════════════════════════════
                    %s═══════════════════════════════════════════
                      [T] Tocar todas as recomendações
                      [0] Voltar ao Menu Principal
                    ═══════════════════════════════════════════
                    Selecione um item ou opção:\s""",
                    formatarRecomendacoes(recs));

            String input = scanner.nextLine().toUpperCase();
            UtilConsole.limparConsole();

            switch (input) {
                case "0" -> noMenu = false;
                case "T" -> {
                    if (!recs.isEmpty()) {
                        if (player.definirFila(recs)) {
                            System.out.println("Fila de reprodução atualizada com " + recs.size() + " itens.");
                            player.play();
                            Audio a = player.getAudioAtual();
                            if (a != null) {
                                System.out.println("▶ Tocando: " + a.getDetalhesFormatados());
                            }
                        }
                        menuPlayer.exibir();
                        UtilConsole.limparConsole();
                    } else {
                        System.out.println("Lista vazia.");
                    }
                }
                default -> {
                    try {
                        int idx = Integer.parseInt(input) - 1;
                        if (idx >= 0 && idx < recs.size()) {
                            if (!exibirDetalhesAudio(recs.get(idx))) {
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
    }

    // ===== Métodos auxiliares de formatação =====

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

    private String formatarRecomendacoes(List<Audio> recs) {
        if (recs.isEmpty()) {
            return "  (Nenhuma recomendação disponível)\n";
        }
        String resultado = "";
        for (int i = 0; i < recs.size(); i++) {
            Audio a = recs.get(i);
            resultado = resultado + "  " + (i + 1) + ". " + a.getTitulo() + " (❤ " + a.getTotalCurtidas() + ")\n";
        }
        return resultado;
    }

    /**
     * Exibe os detalhes de um áudio com opções de ação.
     * 
     * @return true para continuar no menu, false para sair
     */
    private boolean exibirDetalhesAudio(Audio audio) {
        boolean noDetalhe = true;

        while (noDetalhe) {
            String opcaoPlaylist = (usuario != null && !usuario.getPlaylists().isEmpty())
                    ? "  [A] Adicionar a uma Playlist\n"
                    : "";

            System.out.printf("""

                    ═══════════════════════════════════════════
                           ♪ DETALHES DO ITEM
                    ═══════════════════════════════════════════
                      Título: %s
                      %s
                      ────────────────────────────────────
                      ▶ Reproduções: %d
                      ❤ Curtidas: %d
                    ═══════════════════════════════════════════
                      [R] Reproduzir agora
                      [F] Adicionar à Fila de reprodução
                      [L] Curtir/Descurtir
                    %s  [V] Voltar
                      [0] Voltar ao Menu Principal
                    ═══════════════════════════════════════════
                    >>\s""",
                    audio.getTitulo(),
                    audio.getDetalhesFormatados(),
                    audio.getTotalReproducoes(),
                    audio.getTotalCurtidas(),
                    opcaoPlaylist);

            String cmd = scanner.nextLine().toUpperCase();
            UtilConsole.limparConsole();

            switch (cmd) {
                case "R" -> {
                    List<Audio> fila = new ArrayList<>();
                    fila.add(audio);
                    if (player.definirFila(fila)) {
                        player.play();
                        System.out.println("▶ Tocando: " + audio.getDetalhesFormatados());
                    }
                    menuPlayer.exibir();
                    UtilConsole.limparConsole();
                }
                case "F" -> {
                    if (player.adicionarAFila(audio)) {
                        System.out.println("➕ '" + audio.getTitulo() + "' adicionado à fila (" + player.getTamanhoFila()
                                + " itens)");
                    }
                }
                case "L" -> {
                    if (usuario != null) {
                        boolean curtiu = usuario.curtirAudio(audio);
                        if (curtiu) {
                            System.out.println("❤ Você curtiu: " + audio.getDetalhesFormatados());
                        } else {
                            System.out.println("💔 Curtida removida: " + audio.getDetalhesFormatados());
                        }
                        if (usuarioService != null) {
                            usuarioService.salvarUsuarios();
                        }
                    } else {
                        System.out.println("Usuário não disponível.");
                    }
                }
                case "A" -> {
                    if (usuario != null && !usuario.getPlaylists().isEmpty()) {
                        adicionarAPlaylist(audio);
                    } else {
                        System.out.println("Nenhuma playlist disponível. Crie uma primeiro.");
                    }
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

    private void adicionarAPlaylist(Audio audio) {
        List<Playlist> playlists = usuario.getPlaylists();

        System.out.println("\n--- Suas Playlists ---");
        for (int i = 0; i < playlists.size(); i++) {
            Playlist p = playlists.get(i);
            System.out.println((i + 1) + ". " + p.getNome() + " (" + p.getItens().size() + " músicas)");
        }
        System.out.println("0. Cancelar");
        System.out.print("\nSelecione a playlist: ");

        try {
            String input = scanner.nextLine();
            if (input.equals("0") || input.isEmpty()) {
                return;
            }

            int idx = Integer.parseInt(input) - 1;
            if (idx >= 0 && idx < playlists.size()) {
                Playlist playlist = playlists.get(idx);
                if (playlist.adicionarItem(audio)) {
                    if (usuarioService != null) {
                        usuarioService.salvarUsuarios();
                    }
                    System.out.println("✓ '" + audio.getTitulo() + "' adicionado à '" + playlist.getNome() + "'");
                } else {
                    System.out.println("⚠ '" + audio.getTitulo() + "' já existe na playlist.");
                }
            } else {
                System.out.println("Índice inválido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
        }
    }
}
