package model.midia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Representa um álbum musical no sistema AudioStreaming.
 * 
 * <p>
 * Um álbum contém uma coleção ordenada de faixas (músicas) e está
 * associado a um artista. Implementa {@link Serializable} para
 * permitir persistência em disco.
 * </p>
 * 
 * <h2>Uso de Coleções</h2>
 * <p>
 * Utiliza List&lt;Musica&gt; para as faixas porque:
 * </p>
 * <ul>
 * <li>Mantém a ordem das faixas (importante em álbuns)</li>
 * <li>Permite acesso por índice (faixa 1, 2, 3...)</li>
 * <li>Suporta duplicatas se necessário</li>
 * </ul>
 * 
 * <h2>Exemplo de uso</h2>
 * 
 * <pre>{@code
 * Artista queen = new Artista("Queen");
 * Album album = new Album("A Night at the Opera", queen, 1975);
 * album.adicionarFaixa(new Musica("Bohemian Rhapsody", 354, queen, album));
 * }</pre>
 * 
 * @see Artista
 * @see Musica
 */
public class Album implements Serializable {

    /** Identificador de versão para serialização. */
    private static final long serialVersionUID = 1L;

    /** Identificador único do álbum (UUID). */
    private String id;

    /** Título do álbum. */
    private String titulo;

    /** Artista ou banda do álbum. */
    private Artista artista;

    /** Ano de lançamento do álbum. */
    private int anoLancamento;

    /** Lista ordenada de faixas do álbum. */
    private List<Musica> faixas;

    /**
     * Cria um novo álbum.
     * 
     * @param titulo        Título do álbum
     * @param artista       Artista ou banda
     * @param anoLancamento Ano de lançamento
     */
    public Album(String titulo, Artista artista, int anoLancamento) {
        this.id = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.artista = artista;
        this.anoLancamento = anoLancamento;
        this.faixas = new ArrayList<>();
    }

    /**
     * Adiciona uma faixa ao final do álbum.
     * 
     * @param musica Música a ser adicionada
     */
    public void adicionarFaixa(Musica musica) {
        faixas.add(musica);
    }

    /**
     * Retorna o identificador único do álbum.
     * 
     * @return UUID do álbum
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna o título do álbum.
     * 
     * @return Título do álbum
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Retorna o artista do álbum.
     * 
     * @return Objeto Artista
     */
    public Artista getArtista() {
        return artista;
    }

    /**
     * Retorna o ano de lançamento.
     * 
     * @return Ano de lançamento
     */
    public int getAnoLancamento() {
        return anoLancamento;
    }

    /**
     * Retorna cópia da lista de faixas.
     * 
     * <p>
     * Cópia defensiva para evitar modificações externas.
     * </p>
     * 
     * @return Lista de faixas do álbum
     */
    public List<Musica> getFaixas() {
        return new ArrayList<>(faixas);
    }

    /**
     * Retorna o número total de faixas.
     * 
     * @return Quantidade de músicas no álbum
     */
    public int getTotalFaixas() {
        return faixas.size();
    }

    /**
     * Compara álbuns pela igualdade de ID.
     * 
     * @param o Objeto a comparar
     * @return true se os IDs são iguais
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Album album = (Album) o;
        return Objects.equals(id, album.id);
    }

    /**
     * Gera hash code baseado no ID.
     * 
     * @return Hash code do álbum
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Retorna representação textual do álbum.
     * 
     * @return String no formato "Título (ano) - Artista"
     */
    @Override
    public String toString() {
        return titulo + " (" + anoLancamento + ") - " + artista.getNome();
    }
}
