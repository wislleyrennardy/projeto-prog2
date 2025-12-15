package model.midia;

import interfaces.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Classe abstrata que representa um conteúdo de áudio no sistema
 * AudioStreaming.
 * 
 * <p>
 * Esta classe serve como base para todos os tipos de mídia reproduzível,
 * como músicas e podcasts. Implementa as interfaces {@link Reproduzivel},
 * {@link Serializable} e {@link Comparable} para suportar reprodução,
 * persistência e ordenação.
 * </p>
 * 
 * <h2>Uso de Coleções</h2>
 * <p>
 * Objetos Audio são tipicamente armazenados em:
 * </p>
 * <ul>
 * <li>List&lt;Audio&gt; - Catálogo e playlists (mantém ordem)</li>
 * <li>Set&lt;Audio&gt; - Curtidas do usuário (evita duplicatas)</li>
 * <li>Map&lt;String, List&lt;Audio&gt;&gt; - Índice de busca</li>
 * </ul>
 * 
 * @see Musica
 * @see Podcast
 * @see Reproduzivel
 */
public abstract class Audio implements Reproduzivel, Serializable, Comparable<Audio> {

	/** Identificador de versão para serialização. */
	private static final long serialVersionUID = 1L;

	/** Título do áudio. */
	protected String titulo;

	/** Duração em segundos. */
	protected int duracaoSegundos;

	/** Contador de reproduções. */
	private int totalReproducoes;

	/** Contador de curtidas globais. */
	private int totalCurtidas;

	/**
	 * Construtor para criar um novo áudio.
	 * 
	 * @param titulo          Título do áudio (não pode ser null)
	 * @param duracaoSegundos Duração em segundos (deve ser positivo)
	 */
	public Audio(String titulo, int duracaoSegundos) {
		this.titulo = titulo;
		this.duracaoSegundos = duracaoSegundos;
		this.totalReproducoes = 0;
		this.totalCurtidas = 0;
	}

	/**
	 * Retorna o título do áudio.
	 * 
	 * @return O título do áudio
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * Retorna o total de reproduções acumuladas.
	 * 
	 * @return Número de vezes que o áudio foi reproduzido
	 */
	public int getTotalReproducoes() {
		return totalReproducoes;
	}

	/**
	 * Retorna o total de curtidas (likes) do áudio.
	 * 
	 * @return Número total de curtidas
	 */
	public int getTotalCurtidas() {
		return totalCurtidas;
	}

	/**
	 * Incrementa o contador de reproduções.
	 * Chamado automaticamente pelo PlayerService ao reproduzir.
	 */
	public void incrementarReproducao() {
		this.totalReproducoes++;
	}

	/**
	 * Incrementa o contador de curtidas.
	 * Usado pelo sistema de curtidas do usuário.
	 */
	public void curtir() {
		this.totalCurtidas++;
	}

	/**
	 * Decrementa o contador de curtidas (se maior que zero).
	 * Usado quando um usuário remove sua curtida.
	 */
	public void descurtir() {
		if (this.totalCurtidas > 0) {
			this.totalCurtidas--;
		}
	}

	/**
	 * Compara dois áudios pela igualdade de título.
	 * 
	 * @param o Objeto a ser comparado
	 * @return true se os títulos são iguais
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Audio audio = (Audio) o;
		return Objects.equals(titulo, audio.titulo);
	}

	/**
	 * Gera hash code baseado no título.
	 * 
	 * @return Hash code do áudio
	 */
	@Override
	public int hashCode() {
		return Objects.hash(titulo);
	}

	/**
	 * Compara dois áudios alfabeticamente pelo título (case insensitive).
	 * 
	 * @param o Áudio a ser comparado
	 * @return Valor negativo, zero ou positivo conforme ordenação
	 */
	@Override
	public int compareTo(Audio o) {
		return this.titulo.compareToIgnoreCase(o.titulo);
	}

	/**
	 * Retorna representação textual com detalhes e estatísticas.
	 * 
	 * @return String formatada com título, tipo e estatísticas
	 */
	@Override
	public String toString() {
		return getDetalhesFormatados() + " | Plays: " + totalReproducoes + " | Likes: " + totalCurtidas;
	}
}