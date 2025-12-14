package menu;

import model.midia.Audio;
import model.usuario.Usuario;
import service.UsuarioService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Tela de gerenciamento do perfil do usuário.
 * 
 * <p>
 * Esta classe exibe informações do perfil do usuário e permite
 * gerenciar configurações da conta, visualizar curtidas e
 * alterar/excluir a conta.
 * </p>
 * 
 * <h2>Funcionalidades</h2>
 * <ul>
 * <li>Visualizar informações do perfil</li>
 * <li>Ver lista de itens curtidos</li>
 * <li>Alterar senha</li>
 * <li>Excluir conta</li>
 * </ul>
 * 
 * @see Usuario
 * @see UsuarioService
 */
public class TelaPerfil {

    /** Usuário logado cujo perfil está sendo exibido. */
    private final Usuario usuario;

    /** Serviço de usuários para persistência. */
    private final UsuarioService usuarioService;

    /** Scanner para leitura de entrada do usuário. */
    private final Scanner scanner;

    /**
     * Cria a tela de perfil com dependências necessárias.
     * 
     * @param usuario        Usuário logado
     * @param usuarioService Serviço de persistência
     * @param scanner        Scanner para entrada do usuário
     */
    public TelaPerfil(Usuario usuario, UsuarioService usuarioService, Scanner scanner) {
        this.usuario = usuario;
        this.usuarioService = usuarioService;
        this.scanner = scanner;
    }

    /**
     * Exibe o menu do perfil do usuário.
     * 
     * <p>
     * Mostra informações do perfil e opções de gerenciamento.
     * Retorna false se a conta for excluída (necessário logout).
     * </p>
     * 
     * @return true para continuar logado, false se conta foi excluída
     */
    public boolean exibir() {
        boolean noMenu = true;

        while (noMenu) {
            UtilConsole.limparConsole();

            System.out.printf("""

                    ═══════════════════════════════════════════
                               👤 MEU PERFIL
                    ═══════════════════════════════════════════
                      Nome: %s
                      Email: %s
                      Playlists: %d
                      Total de Curtidas: %d
                    ═══════════════════════════════════════════
                      [C] Ver itens curtidos
                      [S] Alterar senha
                      [X] Excluir conta
                      [V] Voltar
                    ═══════════════════════════════════════════
                    >>\s""",
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getPlaylists().size(),
                    usuario.getCurtidas().size());

            String cmd = scanner.nextLine().toUpperCase();
            UtilConsole.limparConsole();

            switch (cmd) {
                case "C" -> exibirCurtidas();
                case "S" -> alterarSenha();
                case "X" -> {
                    if (excluirConta()) {
                        return false; // Conta excluída, sair
                    }
                }
                case "V" -> noMenu = false;
                default -> {
                }
            }
        }
        return true;
    }

    /**
     * Exibe os itens curtidos com paginação.
     */
    private void exibirCurtidas() {
        List<Audio> curtidas = new ArrayList<>(usuario.getCurtidas());

        if (curtidas.isEmpty()) {
            System.out.println("Você ainda não curtiu nenhum item.");
            System.out.print("Pressione Enter para voltar...");
            scanner.nextLine();
            return;
        }

        final int ITENS_POR_PAGINA = 20;
        int totalPaginas = (int) Math.ceil((double) curtidas.size() / ITENS_POR_PAGINA);
        int paginaAtual = 0;

        boolean navegando = true;
        while (navegando) {
            UtilConsole.limparConsole();

            int inicio = paginaAtual * ITENS_POR_PAGINA;
            int fim = Math.min(inicio + ITENS_POR_PAGINA, curtidas.size());
            String infoPagina = "Página " + (paginaAtual + 1) + "/" + totalPaginas + " (" + curtidas.size() + " itens)";

            System.out.printf("""
                    ═══════════════════════════════════════════
                              ❤ ITENS CURTIDOS
                      %s
                    ═══════════════════════════════════════════
                    %s═══════════════════════════════════════════
                    %s  [V] Voltar
                    ═══════════════════════════════════════════
                    >>\s""",
                    infoPagina,
                    formatarListaCurtidas(curtidas, inicio, fim),
                    formatarNavegacaoPagina(paginaAtual, totalPaginas));

            String cmd = scanner.nextLine().toUpperCase();

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
                }
            }
        }
    }

    private String formatarListaCurtidas(List<Audio> curtidas, int inicio, int fim) {
        String resultado = "";
        for (int i = inicio; i < fim; i++) {
            resultado = resultado + "  " + (i + 1) + ". ❤ " + curtidas.get(i).getDetalhesFormatados() + "\n";
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

    /**
     * Permite alterar a senha do usuário.
     */
    private void alterarSenha() {
        System.out.println("═══════════════════════════════════════════");
        System.out.println("           🔐 ALTERAR SENHA");
        System.out.println("═══════════════════════════════════════════");

        System.out.print("Senha atual: ");
        String senhaAtual = scanner.nextLine();

        if (!usuario.verificarSenha(senhaAtual)) {
            System.out.println("❌ Senha atual incorreta.");
            System.out.print("Pressione Enter para voltar...");
            scanner.nextLine();
            return;
        }

        System.out.print("Nova senha (mín. 4 caracteres): ");
        String novaSenha = scanner.nextLine();

        if (novaSenha.length() < 4) {
            System.out.println("❌ A senha deve ter no mínimo 4 caracteres.");
            System.out.print("Pressione Enter para voltar...");
            scanner.nextLine();
            return;
        }

        System.out.print("Confirmar nova senha: ");
        String confirmacao = scanner.nextLine();

        if (!novaSenha.equals(confirmacao)) {
            System.out.println("❌ As senhas não coincidem.");
            System.out.print("Pressione Enter para voltar...");
            scanner.nextLine();
            return;
        }

        usuario.alterarSenha(novaSenha);
        usuarioService.salvarUsuarios();
        System.out.println("✅ Senha alterada com sucesso!");
        System.out.print("Pressione Enter para voltar...");
        scanner.nextLine();
    }

    /**
     * Exclui a conta do usuário após confirmação de senha.
     * 
     * @return true se a conta foi excluída
     */
    private boolean excluirConta() {
        System.out.println("═══════════════════════════════════════════");
        System.out.println("           ⚠️ EXCLUIR CONTA");
        System.out.println("═══════════════════════════════════════════");
        System.out.println("ATENÇÃO: Esta ação é irreversível!");
        System.out.println("Todas as suas playlists e dados serão perdidos.");
        System.out.println("═══════════════════════════════════════════");

        System.out.print("Digite sua senha para confirmar: ");
        String senha = scanner.nextLine();

        if (!usuario.verificarSenha(senha)) {
            System.out.println("❌ Senha incorreta. Operação cancelada.");
            System.out.print("Pressione Enter para voltar...");
            scanner.nextLine();
            return false;
        }

        System.out.print("Tem certeza? Digite 'EXCLUIR' para confirmar: ");
        String confirmacao = scanner.nextLine();

        if (!confirmacao.equals("EXCLUIR")) {
            System.out.println("Operação cancelada.");
            System.out.print("Pressione Enter para voltar...");
            scanner.nextLine();
            return false;
        }

        usuarioService.removerUsuario(usuario.getEmail());
        System.out.println("✅ Conta excluída com sucesso.");
        return true;
    }
}
