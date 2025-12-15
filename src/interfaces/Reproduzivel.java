package interfaces;

/**
 * Interface que define o comportamento de itens reproduzíveis no sistema.
 * 
 * <p>
 * Esta interface estabelece um contrato que todas as mídias reproduzíveis
 * (como Música e Podcast) devem seguir. Isso permite que o sistema trate
 * diferentes tipos de mídia de forma uniforme através do polimorfismo.
 * </p>
 * 
 * <h2>Conceito de POO: Abstração por Contrato</h2>
 * <p>
 * A interface define "o quê" deve ser feito, mas não "como". Cada classe
 * que implementa esta interface decide sua própria implementação.
 * </p>
 * 
 * <h2>Exemplo de uso</h2>
 * 
 * <pre>{@code
 * Reproduzivel item = new Musica("Título", 180, artista);
 * item.reproduzir(); // Polimorfismo em ação
 * item.pausar();
 * }</pre>
 * 
 * @see model.midia.Audio
 * @see model.midia.Musica
 * @see model.midia.Podcast
 */
public interface Reproduzivel {

	/**
	 * Inicia a reprodução do item de mídia.
	 * 
	 * <p>
	 * Este método deve exibir uma mensagem no console indicando
	 * que a reprodução foi iniciada, com detalhes do item.
	 * </p>
	 */
	void reproduzir();

	/**
	 * Pausa a reprodução do item de mídia.
	 * 
	 * <p>
	 * Este método deve exibir uma mensagem no console indicando
	 * que a reprodução foi pausada.
	 * </p>
	 */
	void pausar();

	/**
	 * Retorna uma string formatada com os detalhes do item.
	 * 
	 * <p>
	 * A string retornada deve conter informações suficientes
	 * para identificar o item, como título e tipo.
	 * </p>
	 * 
	 * @return String formatada no padrão "[Tipo] Título (detalhes)"
	 */
	String getDetalhesFormatados();
}