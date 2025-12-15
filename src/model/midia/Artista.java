package model.midia;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Representa um artista musical no sistema AudioStreaming.
 * 
 * <p>
 * Esta classe armazena informações sobre artistas, incluindo
 * seus gêneros musicais. Implementa {@link Serializable} para
 * permitir persistência em disco.
 * </p>
 * 
 * <h2>Uso de Coleções</h2>
 * <p>
 * Utiliza Set&lt;String&gt; para gêneros musicais porque:
 * </p>
 * <ul>
 * <li>Evita gêneros duplicados automaticamente</li>
 * <li>Busca eficiente O(1) para verificar se contém um gênero</li>
 * <li>A ordem dos gêneros não é importante</li>
 * </ul>
 * 
 * <h2>Exemplo de uso</h2>
 * 
 * <pre>{@code
 * Artista artista = new Artista("Queen");
 * artista.adicionarGenero("Rock");
 * artista.adicionarGenero("Classic Rock");
 * }</pre>
 * 
 * @see Musica
 * @see Album
 */
public class Artista implements Serializable {

    /** Identificador de versão para serialização. */
    private static final long serialVersionUID = 1L;

    /** Identificador único do artista (UUID). */
    private String id;

    /** Nome do artista ou banda. */
    private String nome;

    /** Conjunto de gêneros musicais associados ao artista. */
    private Set<String> generos;

    /**
     * Cria um novo artista apenas com nome.
     * 
     * <p>
     * Gera um UUID automaticamente e inicializa set de gêneros vazio.
     * </p>
     * 
     * @param nome Nome do artista ou banda
     */
    public Artista(String nome) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.generos = new HashSet<>();
    }

    /**
     * Cria um novo artista com nome e gêneros.
     * 
     * @param nome    Nome do artista ou banda
     * @param generos Conjunto inicial de gêneros musicais
     */
    public Artista(String nome, Set<String> generos) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        // Cria cópia defensiva para evitar modificações externas
        this.generos = generos != null ? new HashSet<>(generos) : new HashSet<>();
    }

    /**
     * Adiciona um gênero musical ao artista.
     * 
     * <p>
     * Como usa Set, gêneros duplicados são ignorados automaticamente.
     * </p>
     * 
     * @param genero Nome do gênero a adicionar
     */
    public void adicionarGenero(String genero) {
        generos.add(genero);
    }

    /**
     * Retorna o identificador único do artista.
     * 
     * @return UUID do artista
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna o nome do artista.
     * 
     * @return Nome do artista
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna uma cópia do conjunto de gêneros.
     * 
     * <p>
     * Retorna cópia defensiva para evitar que o chamador
     * modifique o conjunto interno diretamente.
     * </p>
     * 
     * @return Cópia do conjunto de gêneros
     */
    public Set<String> getGeneros() {
        return new HashSet<>(generos);
    }

    /**
     * Compara artistas pela igualdade de ID.
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
        Artista artista = (Artista) o;
        return Objects.equals(id, artista.id);
    }

    /**
     * Gera hash code baseado no ID.
     * 
     * @return Hash code do artista
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Retorna representação textual (apenas o nome).
     * 
     * @return Nome do artista
     */
    @Override
    public String toString() {
        return nome;
    }
}
