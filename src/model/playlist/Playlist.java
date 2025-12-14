package model.playlist;

import model.midia.Audio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma playlist personalizada do usuário.
 * 
 * <p>
 * Armazena uma coleção ordenada de itens de áudio (músicas e podcasts)
 * usando List&lt;Audio&gt; para manter a ordem de inserção e permitir
 * acesso por índice.
 * </p>
 * 
 * <h2>Uso de Coleções</h2>
 * <p>
 * Usa List&lt;Audio&gt; porque:
 * </p>
 * <ul>
 * <li>Mantém ordem de inserção (importante para playlists)</li>
 * <li>Permite acesso por índice</li>
 * <li>Suporta iteração sequencial</li>
 * </ul>
 * 
 * <h2>Exemplo de uso</h2>
 * 
 * <pre>{@code
 * Playlist rock = new Playlist("Rock Classics");
 * rock.adicionarItem(musica);
 * for (Audio item : rock.getItens()) {
 * 	item.reproduzir();
 * }
 * }</pre>
 * 
 * @see Audio
 */
public class Playlist implements Serializable {

	/** Identificador de versão para serialização. */
	private static final long serialVersionUID = 1L;

	/** Nome da playlist. */
	private String nome;

	/** Lista de itens de áudio na playlist. */
	private List<Audio> itens;

	/**
	 * Cria uma nova playlist vazia.
	 * 
	 * @param nome Nome da playlist
	 */
	public Playlist(String nome) {
		this.nome = nome;
		this.itens = new ArrayList<>();
	}

	/**
	 * Adiciona um item à playlist, evitando duplicatas.
	 * 
	 * <p>
	 * Verifica se o item já existe antes de adicionar.
	 * A comparação é feita usando o método {@link Audio#equals(Object)}.
	 * </p>
	 * 
	 * @param item O item de áudio a ser adicionado
	 * @return {@code true} se o item foi adicionado, {@code false} se já existia
	 */
	public boolean adicionarItem(Audio item) {
		if (itens.contains(item)) {
			return false;
		}
		itens.add(item);
		return true;
	}

	/**
	 * Remove um item da playlist.
	 * 
	 * @param item O item a ser removido
	 */
	public void removerItem(Audio item) {
		itens.remove(item);
	}

	/**
	 * Retorna a lista de itens da playlist.
	 * 
	 * <p>
	 * <b>Nota:</b> Retorna a lista interna diretamente para eficiência.
	 * Modificações afetarão a playlist.
	 * </p>
	 * 
	 * @return Lista de itens de áudio
	 */
	public List<Audio> getItens() {
		return itens;
	}

	/**
	 * Retorna o nome da playlist.
	 * 
	 * @return Nome da playlist
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Retorna representação textual da playlist.
	 * 
	 * @return String no formato "Playlist: Nome (X itens)"
	 */
	@Override
	public String toString() {
		return "Playlist: " + nome + " (" + itens.size() + " itens)";
	}
}