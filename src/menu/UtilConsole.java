package menu;

import java.util.Scanner;

/**
 * Classe utilitária para operações de console.
 * 
 * <p>
 * Contém métodos estáticos para operações comuns de interface
 * de console, como limpar tela, pausar execução e formatar texto.
 * </p>
 * 
 * <h2>Compatibilidade</h2>
 * <p>
 * Os métodos funcionam tanto em Windows quanto em sistemas Unix.
 * Caso o console não suporte os comandos, usa fallback simples.
 * </p>
 * 
 * <h2>Padrão de Projeto</h2>
 * <p>
 * Classe utilitária (utility class): construtor privado impede
 * instanciação, todos os métodos são estáticos.
 * </p>
 */
public final class UtilConsole {

    /**
     * Construtor privado para impedir instanciação.
     * 
     * <p>
     * Esta é uma classe utilitária com apenas métodos estáticos.
     * </p>
     */
    private UtilConsole() {
        // Classe utilitária - não deve ser instanciada
    }

    /**
     * Limpa o console (funciona em Windows e Unix).
     */
    public static void limparConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++)
                System.out.println();
        }
    }

    /**
     * Pausa a execução até o usuário pressionar Enter.
     * 
     * @param scanner Scanner para leitura de entrada
     */
    public static void pausar(Scanner scanner) {
        System.out.print("\nPressione Enter para continuar...");
        scanner.nextLine();
    }

    /**
     * Formata um nome com padding à direita, truncando se necessário.
     * 
     * @param nome    Texto a formatar
     * @param tamanho Tamanho total desejado
     * @return Texto formatado com padding
     */
    public static String formatarNome(String nome, int tamanho) {
        if (nome.length() > tamanho) {
            return nome.substring(0, tamanho - 3) + "...";
        }
        return String.format("%-" + tamanho + "s", nome);
    }
}
