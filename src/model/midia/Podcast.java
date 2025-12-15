package model.midia;

/**
 * Representa um episódio de podcast no sistema AudioStreaming.
 * 
 * <p>
 * Estende a classe {@link Audio} e adiciona informações específicas
 * de podcasts como apresentador e número do episódio.
 * </p>
 * 
 * <h2>Exemplo de uso</h2>
 * 
 * <pre>{@code
 * Podcast ep = new Podcast("Tech News", 1200, "TechDaily", 42);
 * ep.reproduzir();
 * }</pre>
 * 
 * @see Audio
 */
public class Podcast extends Audio {

	/** Identificador de versão para serialização. */
	private static final long serialVersionUID = 1L;

	/** Nome do apresentador ou programa. */
	private String apresentador;

	/** Número sequencial do episódio. */
	private int numeroEpisodio;

	/**
	 * Cria um novo episódio de podcast.
	 * 
	 * @param titulo          Título do episódio
	 * @param duracaoSegundos Duração em segundos
	 * @param apresentador    Nome do apresentador ou programa
	 * @param numeroEpisodio  Número do episódio na série
	 */
	public Podcast(String titulo, int duracaoSegundos, String apresentador, int numeroEpisodio) {
		super(titulo, duracaoSegundos);
		this.apresentador = apresentador;
		this.numeroEpisodio = numeroEpisodio;
	}

	/**
	 * Retorna o nome do apresentador.
	 * 
	 * @return Nome do apresentador
	 */
	public String getApresentador() {
		return apresentador;
	}

	/**
	 * Retorna o número do episódio.
	 * 
	 * @return Número do episódio
	 */
	public int getNumeroEpisodio() {
		return numeroEpisodio;
	}

	/**
	 * Simula a reprodução do podcast.
	 * A exibição de mensagens é responsabilidade do pacote menu.
	 */
	@Override
	public void reproduzir() {
		// Execução silenciosa - a exibição é gerenciada pelo menu
	}

	/**
	 * Simula a pausa do podcast.
	 * A exibição de mensagens é responsabilidade do pacote menu.
	 */
	@Override
	public void pausar() {
		// Execução silenciosa - a exibição é gerenciada pelo menu
	}

	/**
	 * Retorna detalhes formatados para exibição.
	 * 
	 * @return String no formato "[Podcast] Título com Apresentador"
	 */
	@Override
	public String getDetalhesFormatados() {
		return "[Podcast] " + titulo + " com " + apresentador;
	}
}