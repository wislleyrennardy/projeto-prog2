package model.midia;

/**
 * Representa uma música no sistema AudioStreaming.
 * 
 * <p>
 * Estende a classe abstrata {@link Audio} e adiciona referências
 * específicas a {@link Artista} e {@link Album}. Implementa os
 * métodos abstratos de reprodução definidos pela interface
 * {@link interfaces.Reproduzivel}.
 * </p>
 * 
 * <h2>Herança</h2>
 * <p>
 * Esta classe demonstra herança em Java:
 * </p>
 * <ul>
 * <li>Herda atributos: titulo, duracaoSegundos, estatísticas</li>
 * <li>Herda métodos: getTitulo(), curtir(), etc.</li>
 * <li>Sobrescreve: reproduzir(), pausar(), getDetalhesFormatados()</li>
 * </ul>
 * 
 * @see Audio
 * @see Artista
 * @see Album
 */
public class Musica extends Audio {

	/** Identificador de versão para serialização. */
	private static final long serialVersionUID = 2L;

	/** Artista que interpreta a música. */
	private Artista artista;

	/** Álbum ao qual a música pertence (pode ser null para singles). */
	private Album album;

	/**
	 * Cria uma música com artista e álbum.
	 * 
	 * @param titulo          Título da música
	 * @param duracaoSegundos Duração em segundos
	 * @param artista         Artista que interpreta
	 * @param album           Álbum ao qual pertence
	 */
	public Musica(String titulo, int duracaoSegundos, Artista artista, Album album) {
		super(titulo, duracaoSegundos); // Chama construtor da superclasse
		this.artista = artista;
		this.album = album;
	}

	/**
	 * Cria uma música apenas com artista (single, sem álbum).
	 * 
	 * @param titulo          Título da música
	 * @param duracaoSegundos Duração em segundos
	 * @param artista         Artista que interpreta
	 */
	public Musica(String titulo, int duracaoSegundos, Artista artista) {
		super(titulo, duracaoSegundos);
		this.artista = artista;
		this.album = null; // Single, sem álbum
	}

	/**
	 * Retorna o objeto Artista.
	 * 
	 * @return Artista da música
	 */
	public Artista getArtista() {
		return artista;
	}

	/**
	 * Retorna o nome do artista como String.
	 * 
	 * <p>
	 * Retorna "Desconhecido" se artista for null.
	 * </p>
	 * 
	 * @return Nome do artista
	 */
	public String getNomeArtista() {
		return artista != null ? artista.getNome() : "Desconhecido";
	}

	/**
	 * Retorna o objeto Album.
	 * 
	 * @return Álbum da música, ou null se for single
	 */
	public Album getAlbum() {
		return album;
	}

	/**
	 * Retorna o nome do álbum como String.
	 * 
	 * <p>
	 * Retorna "Single" se a música não pertencer a um álbum.
	 * </p>
	 * 
	 * @return Nome do álbum ou "Single"
	 */
	public String getNomeAlbum() {
		return album != null ? album.getTitulo() : "Single";
	}

	/**
	 * Simula a reprodução da música.
	 * 
	 * <p>
	 * Este método é chamado pelo {@link service.PlayerService}
	 * quando a música deve começar a tocar. A exibição de
	 * mensagens é responsabilidade do pacote menu.
	 * </p>
	 */
	@Override
	public void reproduzir() {
		// Execução silenciosa - a exibição é gerenciada pelo menu
	}

	/**
	 * Simula a pausa da música.
	 * A exibição de mensagens é responsabilidade do pacote menu.
	 */
	@Override
	public void pausar() {
		// Execução silenciosa - a exibição é gerenciada pelo menu
	}

	/**
	 * Retorna detalhes formatados para exibição em listas.
	 * 
	 * @return String no formato "[Música] Título (Artista)"
	 */
	@Override
	public String getDetalhesFormatados() {
		return "[Música] " + titulo + " (" + getNomeArtista() + ")";
	}
}
