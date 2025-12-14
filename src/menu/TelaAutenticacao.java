package menu;

import exception.ValidacaoException;
import model.usuario.Usuario;
import service.UsuarioService;

import java.io.Console;
import java.util.Scanner;

/**
 * Tela de autenticação do sistema AudioStreaming.
 * 
 * <p>
 * Esta classe gerencia o fluxo de login e cadastro de usuários.
 * É exibida ao iniciar a aplicação e retorna o usuário autenticado.
 * </p>
 * 
 * <h2>Fluxos Disponíveis</h2>
 * <ul>
 * <li>Login - Autentica usuário existente</li>
 * <li>Cadastro - Cria novo usuário</li>
 * <li>Sair - Encerra a aplicação</li>
 * </ul>
 * 
 * <h2>Segurança</h2>
 * <p>
 * Tenta usar Console.readPassword() para ocultar senha digitada.
 * Se não disponível (ex: IDE), usa Scanner normal.
 * </p>
 * 
 * @see UsuarioService
 * @see Usuario
 */
public class TelaAutenticacao {

    /** Scanner para leitura de entrada do usuário. */
    private final Scanner scanner;

    /** Serviço de usuários para autenticação e cadastro. */
    private final UsuarioService usuarioService;

    /** Console para leitura segura de senha (pode ser null em IDEs). */
    private final Console console;

    /**
     * Cria a tela de autenticação com dependências.
     * 
     * @param scanner        Scanner para entrada do usuário
     * @param usuarioService Serviço de autenticação
     */
    public TelaAutenticacao(Scanner scanner, UsuarioService usuarioService) {
        this.scanner = scanner;
        this.usuarioService = usuarioService;
        this.console = System.console(); // Pode ser null em IDEs
    }

    /**
     * Exibe a tela de autenticação e processa escolha do usuário.
     * 
     * <p>
     * Executa em loop até o usuário fazer login, cadastrar
     * ou optar por sair.
     * </p>
     * 
     * @return Usuário logado, ou null se optar por sair
     */
    public Usuario exibir() {
        while (true) {
            System.out.print("""

                    ═══════════════════════════════════════════
                            🎵 AudioStreamer 🎵
                       Sistema de Streaming de Música
                    ═══════════════════════════════════════════
                      1. 🔑 Login
                      2. 📝 Cadastrar
                      0. 🚪 Sair
                    ═══════════════════════════════════════════
                    Escolha uma opção:\s""");

            String opcao = scanner.nextLine();
            UtilConsole.limparConsole();

            switch (opcao) {
                case "1":
                    Usuario logado = fluxoLogin();
                    if (logado != null)
                        return logado;
                    break;
                case "2":
                    Usuario novo = fluxoCadastro();
                    if (novo != null)
                        return novo;
                    break;
                case "0":
                    return null;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    /**
     * Lê uma senha de forma segura, ocultando os caracteres digitados.
     * Usa Console.readPassword() quando disponível, ou fallback para Scanner.
     * 
     * @param prompt Mensagem exibida ao usuário
     * @return A senha digitada
     */
    private String lerSenha(String prompt) {
        System.out.print(prompt);
        if (console != null) {
            char[] senhaChars = console.readPassword();
            return senhaChars != null ? new String(senhaChars) : "";
        } else {
            // Fallback para IDEs que não suportam Console
            return scanner.nextLine();
        }
    }

    private Usuario fluxoLogin() {
        System.out.println("--- Login ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        String senha = lerSenha("Senha: ");

        try {
            Usuario usuario = usuarioService.login(email, senha);
            System.out.println("Bem-vindo(a) de volta, " + usuario.getNome() + "!");
            UtilConsole.pausar(scanner);
            return usuario;
        } catch (ValidacaoException e) {
            System.out.println("Erro: " + e.getMessage());
            UtilConsole.pausar(scanner);
            return null;
        }
    }

    private Usuario fluxoCadastro() {
        System.out.println("--- Cadastro de Novo Usuário ---");
        System.out.print("Email (para login): ");
        String email = scanner.nextLine();
        System.out.print("Nome de exibição: ");
        String nome = scanner.nextLine();
        String senha = lerSenha("Senha (mín. 4 caracteres): ");
        String confirmacao = lerSenha("Confirmar senha: ");

        if (!senha.equals(confirmacao)) {
            System.out.println("Erro: As senhas não coincidem.");
            UtilConsole.pausar(scanner);
            return null;
        }

        try {
            Usuario usuario = usuarioService.cadastrar(email, senha, nome);
            System.out.println("Usuário '" + usuario.getNome() + "' cadastrado com sucesso!");
            UtilConsole.pausar(scanner);
            return usuario;
        } catch (ValidacaoException e) {
            System.out.println("Erro: " + e.getMessage());
            UtilConsole.pausar(scanner);
            return null;
        }
    }
}
