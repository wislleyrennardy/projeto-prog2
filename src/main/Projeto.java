package main;

import menu.*;
import model.usuario.Usuario;
import service.*;

import java.util.Scanner;

/**
 * Classe principal do sistema AudioStreaming.
 * 
 * <p>
 * Esta classe contém o método {@link #main(String[])} que é o ponto
 * de entrada da aplicação. Responsável por:
 * </p>
 * <ol>
 * <li>Inicializar os serviços (biblioteca, usuários, player)</li>
 * <li>Carregar dados persistidos do disco</li>
 * <li>Exibir tela de autenticação</li>
 * <li>Executar o loop principal do menu</li>
 * <li>Salvar dados ao encerrar</li>
 * </ol>
 * 
 * <h2>Fluxo de Execução</h2>
 * 
 * <pre>
 *   main()
 *     ├── Carregar dados (catálogo + usuários)
 *     ├── TelaAutenticacao.exibir() → Usuario
 *     └── MenuPrincipal.executarLoop()
 * </pre>
 * 
 */
public class Projeto {

	/**
	 * Ponto de entrada da aplicação AudioStreaming.
	 * 
	 * <p>
	 * Inicializa todos os componentes do sistema e inicia
	 * o fluxo de autenticação seguido do menu principal.
	 * </p>
	 * 
	 * @param args Argumentos de linha de comando (não utilizados)
	 */
	public static void main(String[] args) {
		// Cria o Scanner para leitura de entrada do usuário
		Scanner scanner = new Scanner(System.in);

		// Obtém instâncias dos serviços (Singleton)
		BibliotecaService biblioteca = BibliotecaService.getInstance();
		UsuarioService usuarioService = UsuarioService.getInstance();

		// Cria nova instância do player para esta sessão
		PlayerService player = new PlayerService();

		// Carregar dados persistidos do disco
		biblioteca.carregarDadosDoDisco();
		usuarioService.carregarUsuarios();

		UtilConsole.limparConsole();

		// ==== FLUXO DE AUTENTICAÇÃO ====
		// Exibe tela de login/cadastro até obter usuário válido
		TelaAutenticacao telaAuth = new TelaAutenticacao(scanner, usuarioService);
		Usuario usuarioLogado = telaAuth.exibir();

		// Se usuário optou por sair (retornou null), encerra
		if (usuarioLogado == null) {
			System.out.println("Encerrando AudioStreaming...");
			scanner.close();
			return;
		}

		// ==== MENU PRINCIPAL ====
		// Executa loop principal até usuário sair
		UtilConsole.limparConsole();
		MenuPrincipal menu = new MenuPrincipal(scanner, usuarioLogado, biblioteca, usuarioService, player);
		menu.executarLoop();

		// Fecha recursos
		scanner.close();
	}
}
