package service;

import model.midia.*;

import java.io.*;
import java.util.*;

/**
 * Serviço responsável pelo gerenciamento do catálogo global de áudios.
 * 
 * <p>
 * Esta classe implementa o padrão Singleton para fornecer acesso
 * global ao catálogo de músicas e podcasts. Também gerencia a
 * persistência dos dados em disco e mantém um índice para busca rápida.
 * </p>
 * 
 * <h2>Padrão Singleton</h2>
 * <p>
 * O padrão Singleton garante que exista apenas uma instância desta
 * classe em toda a aplicação. Isso é importante porque:
 * </p>
 * <ul>
 * <li>O catálogo deve ser o mesmo em todo o sistema</li>
 * <li>Evita duplicação de dados em memória</li>
 * <li>Simplifica o acesso via {@link #getInstance()}</li>
 * </ul>
 * 
 * <h2>Uso de Coleções</h2>
 * <ul>
 * <li>List&lt;Audio&gt; - Catálogo principal, mantém ordem de inserção</li>
 * <li>Map&lt;String, List&lt;Audio&gt;&gt; - Índice de busca rápida O(1)</li>
 * <li>Map&lt;String, Artista&gt; - Cache de artistas para evitar
 * duplicatas</li>
 * </ul>
 * 
 * <h2>Persistência</h2>
 * <p>
 * Os dados são salvos/carregados via serialização Java no arquivo
 * "catalogo.db".
 * </p>
 * 
 * @see Audio
 * @see PlayerService
 */
public class BibliotecaService {

    /** Instância única do serviço (Singleton). */
    private static BibliotecaService instance;

    /** Nome do arquivo para persistência do catálogo. */
    private static final String ARQUIVO_CATALOGO = "catalogo.db";

    /**
     * Catálogo principal de áudios.
     * Usa List para manter ordem de inserção e permitir iteração sequencial.
     */
    private List<Audio> catalogo;

    /**
     * Índice de busca rápida por termo.
     * Chave: termo em minúsculo, Valor: lista de áudios correspondentes.
     * Permite busca em O(1) por termos exatos.
     */
    private Map<String, List<Audio>> indiceBusca;

    /**
     * Cache de artistas para evitar criar duplicatas.
     * Chave: nome em minúsculo, Valor: objeto Artista.
     */
    private Map<String, Artista> artistas;

    /**
     * Construtor privado (Singleton).
     * 
     * <p>
     * Inicializa o catálogo e os índices vazios.
     * </p>
     */
    private BibliotecaService() {
        catalogo = new ArrayList<>();
        indiceBusca = new HashMap<>();
        artistas = new HashMap<>();
    }

    /**
     * Retorna a instância única do serviço (Singleton).
     * 
     * <p>
     * Cria a instância na primeira chamada (lazy initialization).
     * Chamadas subsequentes retornam a mesma instância.
     * </p>
     * 
     * @return Instância única do BibliotecaService
     */
    public static BibliotecaService getInstance() {
        if (instance == null) {
            instance = new BibliotecaService();
        }
        return instance;
    }

    /**
     * Obtém um artista existente ou cria um novo pelo nome.
     * 
     * <p>
     * Evita duplicação de artistas usando um cache Map.
     * A busca é case-insensitive (ignora maiúsculas/minúsculas).
     * </p>
     * 
     * @param nome Nome do artista
     * @return Artista existente ou recém-criado
     */
    public Artista getOuCriarArtista(String nome) {
        String chave = nome.toLowerCase();
        if (!artistas.containsKey(chave)) {
            artistas.put(chave, new Artista(nome));
        }
        return artistas.get(chave);
    }

    /**
     * Adiciona item e atualiza o índice de busca.
     */
    public void adicionarAudio(Audio audio) {
        catalogo.add(audio);
        indexarAudio(audio);
    }

    /**
     * Cria entradas no Map de índice baseadas no título e (se for música) no
     * artista.
     */
    private void indexarAudio(Audio audio) {
        // Indexar por título
        String chaveTitulo = audio.getTitulo().toLowerCase();
        adicionarAoIndice(chaveTitulo, audio);

        // Se for música, indexar por artista
        if (audio instanceof Musica) {
            String chaveArtista = ((Musica) audio).getNomeArtista().toLowerCase();
            adicionarAoIndice(chaveArtista, audio);
        }
    }

    private void adicionarAoIndice(String chave, Audio audio) {
        if (!indiceBusca.containsKey(chave)) {
            indiceBusca.put(chave, new ArrayList<>());
        }
        indiceBusca.get(chave).add(audio);
    }

    /**
     * Busca O(1) média no Map ou O(N) parcial se for substring.
     */
    public List<Audio> buscar(String termo) {
        termo = termo.toLowerCase();

        // Tentativa de busca exata no índice (muito rápido)
        if (indiceBusca.containsKey(termo)) {
            return indiceBusca.get(termo);
        }

        // Fallback: Busca linear parcial (contém)
        List<Audio> resultados = new ArrayList<>();
        for (Audio a : catalogo) {
            if (a.getTitulo().toLowerCase().contains(termo)) {
                resultados.add(a);
            } else if (a instanceof Musica) {
                if (((Musica) a).getNomeArtista().toLowerCase().contains(termo)) {
                    resultados.add(a);
                }
            }
        }
        return resultados;
    }

    public List<Audio> getCatalogo() {
        return catalogo;
    }

    /**
     * Retorna lista ordenada por popularidade.
     * Critério primário: curtidas (decrescente)
     * Critério de desempate: reproduções (decrescente)
     */
    public List<Audio> getCatalogoPorPopularidade() {
        List<Audio> ordenado = new ArrayList<>(catalogo);
        Collections.sort(ordenado, new Comparator<Audio>() {
            @Override
            public int compare(Audio o1, Audio o2) {
                // Primeiro compara por curtidas (decrescente)
                int comparacaoCurtidas = Integer.compare(o2.getTotalCurtidas(), o1.getTotalCurtidas());
                if (comparacaoCurtidas != 0) {
                    return comparacaoCurtidas;
                }
                // Em caso de empate, compara por reproduções (decrescente)
                return Integer.compare(o2.getTotalReproducoes(), o1.getTotalReproducoes());
            }
        });
        return ordenado;
    }

    /**
     * Recomendação simples: Retorna os itens mais curtidos.
     */
    public List<Audio> recomendarMaisCurtidos() {
        List<Audio> top = getCatalogoPorPopularidade();
        // Retorna top 5 ou tamanho total
        return top.subList(0, Math.min(5, top.size()));
    }

    // Persistência: Carregar/Salvar estado (Serialização)
    /**
     * Carrega dados do disco.
     * Operação silenciosa - não imprime mensagens.
     * 
     * @return true se carregou com sucesso, false se usou dados iniciais
     */
    @SuppressWarnings("unchecked")
    public boolean carregarDadosDoDisco() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_CATALOGO))) {
            this.catalogo = (List<Audio>) ois.readObject();
            // Recriar índice após carregar
            this.indiceBusca = new HashMap<>();
            this.artistas = new HashMap<>();
            for (Audio a : catalogo) {
                indexarAudio(a);
                // Reconstruir mapa de artistas
                if (a instanceof Musica) {
                    Artista artista = ((Musica) a).getArtista();
                    if (artista != null) {
                        artistas.put(artista.getNome().toLowerCase(), artista);
                    }
                }
            }
            return true;
        } catch (FileNotFoundException e) {
            // Arquivo não existe - criar catálogo padrão
            carregarDadosIniciais();
            return false;
        } catch (IOException | ClassNotFoundException e) {
            // Erro ao ler - criar catálogo padrão
            carregarDadosIniciais();
            return false;
        }
    }

    /**
     * Salva dados no disco.
     * Operação silenciosa - não imprime mensagens.
     * 
     * @return true se salvou com sucesso, false em caso de erro
     */
    public boolean salvarDadosNoDisco() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_CATALOGO))) {
            oos.writeObject(catalogo);
            return true;
        } catch (IOException e) {
            // Erro silencioso - log poderia ser adicionado aqui
            return false;
        }
    }

    private void carregarDadosIniciais() {
        if (!catalogo.isEmpty())
            return;

        // Criar artistas
        Artista queen = getOuCriarArtista("Queen");
        queen.adicionarGenero("Rock");
        queen.adicionarGenero("Classic Rock");

        Artista eagles = getOuCriarArtista("Eagles");
        eagles.adicionarGenero("Rock");
        eagles.adicionarGenero("Country Rock");

        Artista edSheeran = getOuCriarArtista("Ed Sheeran");
        edSheeran.adicionarGenero("Pop");
        edSheeran.adicionarGenero("Folk");

        Artista theWeeknd = getOuCriarArtista("The Weeknd");
        theWeeknd.adicionarGenero("R&B");
        theWeeknd.adicionarGenero("Pop");

        Artista daftPunk = getOuCriarArtista("Daft Punk");
        daftPunk.adicionarGenero("Electronic");
        daftPunk.adicionarGenero("House");

        // Criar álbuns
        Album nightAtOpera = new Album("A Night at the Opera", queen, 1975);
        Album hotelCaliforniaAlbum = new Album("Hotel California", eagles, 1976);
        Album divide = new Album("÷ (Divide)", edSheeran, 2017);
        Album afterHours = new Album("After Hours", theWeeknd, 2020);
        Album randomAccessMemories = new Album("Random Access Memories", daftPunk, 2013);

        // Adicionar músicas com artistas e álbuns
        adicionarAudio(new Musica("Bohemian Rhapsody", 354, queen, nightAtOpera));
        adicionarAudio(new Musica("Love of My Life", 219, queen, nightAtOpera));
        adicionarAudio(new Musica("Hotel California", 390, eagles, hotelCaliforniaAlbum));
        adicionarAudio(new Musica("Shape of You", 233, edSheeran, divide));
        adicionarAudio(new Musica("Castle on the Hill", 261, edSheeran, divide));
        adicionarAudio(new Musica("Blinding Lights", 200, theWeeknd, afterHours));
        adicionarAudio(new Musica("Save Your Tears", 215, theWeeknd, afterHours));
        adicionarAudio(new Musica("Get Lucky", 369, daftPunk, randomAccessMemories));
        adicionarAudio(new Musica("Instant Crush", 337, daftPunk, randomAccessMemories));

        // Adicionar podcasts
        adicionarAudio(new Podcast("Tech News #1", 1200, "TechDaily", 1));
        adicionarAudio(new Podcast("História do Java", 3600, "DevCast", 42));
        adicionarAudio(new Podcast("Carreira em TI", 2400, "DevCast", 43));

        // --- DADOS NORTE/NORDESTE ---

        // Artistas
        Artista luizGonzaga = getOuCriarArtista("Luiz Gonzaga");
        luizGonzaga.adicionarGenero("Forró");
        luizGonzaga.adicionarGenero("Baião");

        Artista dominguinhos = getOuCriarArtista("Dominguinhos");
        dominguinhos.adicionarGenero("Forró");
        dominguinhos.adicionarGenero("Xote");

        Artista alceuValenca = getOuCriarArtista("Alceu Valença");
        alceuValenca.adicionarGenero("MPB");
        alceuValenca.adicionarGenero("Forró");

        Artista zeRamalho = getOuCriarArtista("Zé Ramalho");
        zeRamalho.adicionarGenero("MPB");
        zeRamalho.adicionarGenero("Folk Brasileiro");

        Artista elbaRamalho = getOuCriarArtista("Elba Ramalho");
        elbaRamalho.adicionarGenero("MPB");
        elbaRamalho.adicionarGenero("Forró");

        Artista gilbertoGil = getOuCriarArtista("Gilberto Gil");
        gilbertoGil.adicionarGenero("MPB");
        gilbertoGil.adicionarGenero("Tropicália");

        Artista caetanoVeloso = getOuCriarArtista("Caetano Veloso");
        caetanoVeloso.adicionarGenero("MPB");
        caetanoVeloso.adicionarGenero("Tropicália");

        Artista chicoScience = getOuCriarArtista("Chico Science & Nação Zumbi");
        chicoScience.adicionarGenero("Manguebeat");
        chicoScience.adicionarGenero("Rock");

        Artista iveteSangalo = getOuCriarArtista("Ivete Sangalo");
        iveteSangalo.adicionarGenero("Axé");
        iveteSangalo.adicionarGenero("Pop");

        Artista reginaldoRossi = getOuCriarArtista("Reginaldo Rossi");
        reginaldoRossi.adicionarGenero("Brega");

        Artista fagner = getOuCriarArtista("Fagner");
        fagner.adicionarGenero("MPB");

        Artista belchior = getOuCriarArtista("Belchior");
        belchior.adicionarGenero("MPB");

        Artista novosBaianos = getOuCriarArtista("Novos Baianos");
        novosBaianos.adicionarGenero("MPB");
        novosBaianos.adicionarGenero("Rock");

        Artista bandaCalypso = getOuCriarArtista("Banda Calypso");
        bandaCalypso.adicionarGenero("Calypso");
        bandaCalypso.adicionarGenero("Brega");

        Artista olodum = getOuCriarArtista("Olodum");
        olodum.adicionarGenero("Samba-Reggae");

        // Álbuns
        Album oReiDoBaiao = new Album("O Rei do Baião", luizGonzaga, 1950);
        Album dominguinhosAoVivo = new Album("Dominguinhos Ao Vivo", dominguinhos, 2000);
        Album cavaloDePau = new Album("Cavalo de Pau", alceuValenca, 1982);
        Album avohai = new Album("Avohai", zeRamalho, 1978);
        Album grandeEncontro = new Album("O Grande Encontro", elbaRamalho, 1996); // Coletivo, simplificado
        Album realce = new Album("Realce", gilbertoGil, 1979);
        Album transa = new Album("Transa", caetanoVeloso, 1972);
        Album daLamaAoCaos = new Album("Da Lama ao Caos", chicoScience, 1994);
        Album festa = new Album("Festa", iveteSangalo, 2005);
        Album reiDoBrega = new Album("O Rei do Brega", reginaldoRossi, 1987);
        Album romanceNoDeserto = new Album("Romance no Deserto", fagner, 1987);
        Album alucinacao = new Album("Alucinação", belchior, 1976);
        Album acabouChorare = new Album("Acabou Chorare", novosBaianos, 1972);
        Album volume1 = new Album("Volume 1", bandaCalypso, 1999);
        Album egitoMadagascar = new Album("Egito Madagascar", olodum, 1987);

        // Músicas - Luiz Gonzaga
        adicionarAudio(new Musica("Asa Branca", 195, luizGonzaga, oReiDoBaiao));
        adicionarAudio(new Musica("O Xote das Meninas", 230, luizGonzaga, oReiDoBaiao));
        adicionarAudio(new Musica("Pagode Russo", 210, luizGonzaga, oReiDoBaiao));
        adicionarAudio(new Musica("A Vida do Viajante", 245, luizGonzaga, oReiDoBaiao));
        adicionarAudio(new Musica("Numa Sala de Reboco", 188, luizGonzaga, oReiDoBaiao));

        // Músicas - Dominguinhos
        adicionarAudio(new Musica("Eu Só Quero um Xodó", 205, dominguinhos, dominguinhosAoVivo));
        adicionarAudio(new Musica("Gostoso Demais", 220, dominguinhos, dominguinhosAoVivo));
        adicionarAudio(new Musica("Isso Aqui Tá Bom Demais", 195, dominguinhos, dominguinhosAoVivo));
        adicionarAudio(new Musica("De Volta Pro Aconchego", 240, dominguinhos, dominguinhosAoVivo));

        // Músicas - Alceu Valença
        adicionarAudio(new Musica("Anunciação", 250, alceuValenca, cavaloDePau));
        adicionarAudio(new Musica("Tropicana (Morena Tropicana)", 215, alceuValenca, cavaloDePau));
        adicionarAudio(new Musica("La Belle De Jour", 260, alceuValenca, cavaloDePau));
        adicionarAudio(new Musica("Coração Bobo", 230, alceuValenca, cavaloDePau));
        adicionarAudio(new Musica("Pelas Ruas que Andei", 245, alceuValenca, cavaloDePau));

        // Músicas - Zé Ramalho
        adicionarAudio(new Musica("Chão de Giz", 270, zeRamalho, avohai));
        adicionarAudio(new Musica("Avohai", 300, zeRamalho, avohai));
        adicionarAudio(new Musica("Frevo Mulher", 220, zeRamalho, avohai));
        adicionarAudio(new Musica("Admirável Gado Novo", 290, zeRamalho, avohai));
        adicionarAudio(new Musica("Sinônimos", 310, zeRamalho, avohai));

        // Músicas - Elba Ramalho
        adicionarAudio(new Musica("Banho de Cheiro", 200, elbaRamalho, grandeEncontro));
        adicionarAudio(new Musica("Bate Coração", 210, elbaRamalho, grandeEncontro));
        adicionarAudio(new Musica("Ai Que Saudade de Ocê", 235, elbaRamalho, grandeEncontro));

        // Músicas - Gilberto Gil
        adicionarAudio(new Musica("Aquele Abraço", 280, gilbertoGil, realce));
        adicionarAudio(new Musica("Esperando na Janela", 245, gilbertoGil, realce));
        adicionarAudio(new Musica("Andar com Fé", 210, gilbertoGil, realce));
        adicionarAudio(new Musica("Vamos Fugir", 260, gilbertoGil, realce));

        // Músicas - Caetano Veloso
        adicionarAudio(new Musica("Sozinho", 250, caetanoVeloso, transa));
        adicionarAudio(new Musica("Leãozinho", 205, caetanoVeloso, transa));
        adicionarAudio(new Musica("Você é Linda", 270, caetanoVeloso, transa));
        adicionarAudio(new Musica("Reconvexo", 240, caetanoVeloso, transa));

        // Músicas - Chico Science
        adicionarAudio(new Musica("Da Lama ao Caos", 230, chicoScience, daLamaAoCaos));
        adicionarAudio(new Musica("A Praieira", 215, chicoScience, daLamaAoCaos));
        adicionarAudio(new Musica("Manguetown", 225, chicoScience, daLamaAoCaos));
        adicionarAudio(new Musica("Maracatu Atômico", 244, chicoScience, daLamaAoCaos));

        // Músicas - Ivete Sangalo
        adicionarAudio(new Musica("Sorte Grande", 205, iveteSangalo, festa));
        adicionarAudio(new Musica("Festa", 210, iveteSangalo, festa));
        adicionarAudio(new Musica("Quando a Chuva Passar", 230, iveteSangalo, festa));
        adicionarAudio(new Musica("Abalou", 220, iveteSangalo, festa));

        // Músicas - Reginaldo Rossi
        adicionarAudio(new Musica("Garçom", 240, reginaldoRossi, reiDoBrega));
        adicionarAudio(new Musica("A Raposa e as Uvas", 215, reginaldoRossi, reiDoBrega));
        adicionarAudio(new Musica("Em Plena Lua de Mel", 225, reginaldoRossi, reiDoBrega));

        // Músicas - Fagner
        adicionarAudio(new Musica("Borbulhas de Amor", 235, fagner, romanceNoDeserto));
        adicionarAudio(new Musica("Deslizes", 250, fagner, romanceNoDeserto));
        adicionarAudio(new Musica("Canteiros", 240, fagner, romanceNoDeserto));

        // Músicas - Belchior
        adicionarAudio(new Musica("Apenas um Rapaz Latino-Americano", 255, belchior, alucinacao));
        adicionarAudio(new Musica("Como Nossos Pais", 280, belchior, alucinacao));
        adicionarAudio(new Musica("Velha Roupa Colorida", 260, belchior, alucinacao));

        // Músicas - Novos Baianos
        adicionarAudio(new Musica("Preta Pretinha", 225, novosBaianos, acabouChorare));
        adicionarAudio(new Musica("Mistério do Planeta", 240, novosBaianos, acabouChorare));
        adicionarAudio(new Musica("A Menina Dança", 235, novosBaianos, acabouChorare));

        // Músicas - Banda Calypso
        adicionarAudio(new Musica("A Lua Me Traiu", 210, bandaCalypso, volume1));
        adicionarAudio(new Musica("Dançando Calypso", 200, bandaCalypso, volume1));

        // Músicas - Olodum
        adicionarAudio(new Musica("Faraó", 250, olodum, egitoMadagascar));
        adicionarAudio(new Musica("Requebra", 230, olodum, egitoMadagascar));

        // --- FIM DADOS NORTE/NORDESTE ---
    }
}