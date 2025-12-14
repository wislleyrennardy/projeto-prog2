package menu;

import model.midia.Audio;
import model.usuario.Usuario;
import service.PlayerService;

import java.util.List;
import java.util.Scanner;

/**
 * Menu de controle do player de reprodução de áudio.
 * 
 * <p>
 * Esta classe fornece uma interface interativa para controlar a
 * reprodução de músicas e podcasts. Permite ao usuário executar
 * operações como play, pause, próxima, anterior, shuffle e ordenação.
 * </p>
 * 
 * <h2>Comandos Disponíveis</h2>
 * <ul>
 * <li>[P] Play/Pause - Alterna estado de reprodução</li>
 * <li>[&gt;] Próxima - Avança para próxima faixa</li>
 * <li>[&lt;] Anterior - Volta para faixa anterior</li>
 * <li>[L] Curtir - Adiciona/remove curtida</li>
 * <li>[S] Shuffle - Embaralha a fila</li>
 * <li>[O] Ordenar - Ordena por popularidade</li>
 * <li>[F] Ver Fila - Mostra fila de reprodução</li>
 * <li>[C] Limpar - Remove todos da fila</li>
 * </ul>
 * 
 * @see PlayerService
 * @see Audio
 */
public class MenuPlayer {

    /** Scanner para leitura de entrada do usuário. */
    private final Scanner scanner;

    /** Serviço de controle de reprodução. */
    private final PlayerService player;

    /** Usuário logado para operações de curtida. */
    private final Usuario usuario;

    /**
     * Cria o menu do player com dependências necessárias.
     * 
     * @param scanner Scanner para entrada do usuário
     * @param player  Serviço de reprodução
     * @param usuario Usuário logado (para curtidas)
     */
    public MenuPlayer(Scanner scanner, PlayerService player, Usuario usuario) {
        this.scanner = scanner;
        this.player = player;
        this.usuario = usuario;
    }

    /**
     * Exibe o menu do player e processa comandos do usuário.
     * 
     * <p>
     * Executa em loop até o usuário pressionar [V] para voltar.
     * A cada iteração, exibe o estado atual do player (áudio tocando,
     * status, posição na fila) e aguarda um comando.
     * </p>
     */
    public void exibir() {
        UtilConsole.limparConsole();
        boolean noPlayer = true;

        // Loop principal do menu do player
        while (noPlayer) {
            // Obtém informações do estado atual do player
            Audio atual = player.getAudioAtual();
            String status = player.getStatus();
            int posicao = player.getIndiceAtual() + 1; // +1 para exibição (base 1)
            int total = player.getTamanhoFila();

            // Exibe interface diferente dependendo se há item selecionado
            if (atual != null) {
                // Interface completa com informações do áudio atual
                System.out.printf("""
                        ═══════════════════════════════════════════
                                  🎵 PLAYER
                          Status: %s
                          Fila: %d/%d itens
                        ═══════════════════════════════════════════
                          Título:  %s
                          Detalhe: %s
                        ═══════════════════════════════════════════
                          [P] Play/Pause  [>] Próxima  [<] Anterior
                          [L] Curtir      [S] Shuffle  [O] Ordenar
                          [F] Ver Fila   [C] Limpar Fila
                          [V] Voltar
                        ═══════════════════════════════════════════
                        >>\s""",
                        status,
                        posicao, total,
                        atual.getTitulo(),
                        atual.getDetalhesFormatados());
            } else {
                // Interface simplificada quando não há item selecionado
                System.out.printf("""
                        ═══════════════════════════════════════════
                                  🎵 PLAYER
                          Status: %s
                          Fila: %d itens
                        ═══════════════════════════════════════════
                          (Nenhum item selecionado)
                        ═══════════════════════════════════════════
                          [P] Play   [F] Ver Fila   [C] Limpar Fila
                          [V] Voltar
                        ═══════════════════════════════════════════
                        >>\s""",
                        status,
                        total);
            }

            // Lê e processa comando do usuário
            String cmd = scanner.nextLine().toUpperCase();
            UtilConsole.limparConsole();

            // Processa comando usando switch expression (Java 14+)
            switch (cmd) {
                case "P" -> {
                    // Toggle play/pause
                    if (player.isTocando()) {
                        player.pause();
                        System.out.println("|| Pausado.");
                    } else {
                        if (player.play()) {
                            Audio a = player.getAudioAtual();
                            if (a != null) {
                                System.out.println("▶ Tocando: " + a.getDetalhesFormatados());
                            }
                        } else {
                            System.out.println("A fila está vazia.");
                        }
                    }
                }
                case ">" -> {
                    if (player.proxima()) {
                        Audio a = player.getAudioAtual();
                        if (a != null) {
                            System.out.println("▶ Tocando: " + a.getDetalhesFormatados());
                        }
                    } else {
                        System.out.println("Fim da playlist.");
                    }
                }
                case "<" -> {
                    if (player.anterior()) {
                        Audio a = player.getAudioAtual();
                        if (a != null) {
                            System.out.println("▶ Tocando: " + a.getDetalhesFormatados());
                        }
                    } else {
                        System.out.println("Já está no início da playlist.");
                    }
                }
                case "L" -> {
                    // Curtir o áudio atual
                    if (atual != null) {
                        boolean curtiu = usuario.curtirAudio(atual);
                        if (curtiu) {
                            System.out.println("❤ Você curtiu: " + atual.getDetalhesFormatados());
                        } else {
                            System.out.println("💔 Curtida removida: " + atual.getDetalhesFormatados());
                        }
                    }
                }
                case "S" -> {
                    if (player.shuffle()) {
                        System.out.println("🔀 Fila embaralhada!");
                    } else {
                        System.out.println("A fila está vazia.");
                    }
                }
                case "O" -> {
                    if (player.ordenarPorPopularidade()) {
                        System.out.println("📊 Fila ordenada por popularidade!");
                    } else {
                        System.out.println("A fila está vazia.");
                    }
                }
                case "F" -> exibirFila();
                case "C" -> {
                    player.limparFila();
                    System.out.println("🗑 Fila de reprodução limpa.");
                }
                case "V" -> noPlayer = false; // Sai do loop
                default -> {
                    // Comando não reconhecido, ignora
                }
            }
        }
    }

    /**
     * Exibe a lista de áudios na fila de reprodução.
     * 
     * <p>
     * Mostra todos os itens da fila com seus índices, destacando
     * o item atualmente em reprodução com o marcador "▶".
     * </p>
     */
    private void exibirFila() {
        List<Audio> fila = player.getFila();
        int indiceAtual = player.getIndiceAtual();

        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("        📋 FILA DE REPRODUÇÃO");
        System.out.println("═══════════════════════════════════════════");

        if (fila.isEmpty()) {
            System.out.println("  (Fila vazia)");
        } else {
            // Itera sobre a fila exibindo cada item
            for (int i = 0; i < fila.size(); i++) {
                // Marca o item atual com "▶ "
                String marcador = (i == indiceAtual) ? "▶ " : "  ";
                System.out.println(marcador + (i + 1) + ". " + fila.get(i).getTitulo());
            }
        }

        System.out.println("═══════════════════════════════════════════");
        System.out.print("Pressione Enter para voltar...");
        scanner.nextLine();
        UtilConsole.limparConsole();
    }
}
