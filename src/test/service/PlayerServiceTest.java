package test.service;

import model.midia.*;
import service.PlayerService;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o PlayerService usando JUnit 5.
 * Verifica funcionalidades de reprodução, navegação e shuffle.
 * 
 */
@DisplayName("PlayerService Tests")
public class PlayerServiceTest {

    private PlayerService player;
    private Artista artistaTeste;
    private List<Audio> filaTeste;

    /**
     * Configura o ambiente de teste antes de cada método.
     * Cria um novo PlayerService e uma fila de músicas para teste.
     */
    @BeforeEach
    void setUp() {
        player = new PlayerService();
        artistaTeste = new Artista("Artista Teste");
        filaTeste = criarFilaTeste();
    }

    @Test
    @DisplayName("Deve definir fila e posicionar no primeiro item")
    void testDefinirFila() {
        player.definirFila(filaTeste);

        assertEquals(3, player.getTamanhoFila(), "Fila deve ter 3 itens");
        assertEquals(0, player.getIndiceAtual(), "Índice inicial deve ser 0");
    }

    @Test
    @DisplayName("Deve avançar para próxima música")
    void testProxima() {
        player.definirFila(filaTeste);
        player.proxima();

        assertEquals(1, player.getIndiceAtual(), "Índice deve ser 1 após próxima");
    }

    @Test
    @DisplayName("Deve voltar para música anterior")
    void testAnterior() {
        player.definirFila(filaTeste);
        player.proxima(); // vai para 1
        player.anterior(); // volta para 0

        assertEquals(0, player.getIndiceAtual(), "Índice deve voltar para 0");
    }

    @Test
    @DisplayName("Deve embaralhar fila mantendo áudio atual na posição 0")
    void testShuffle() {
        player.definirFila(filaTeste);
        Audio audioAntes = player.getAudioAtual();

        player.shuffle();

        assertEquals(0, player.getIndiceAtual(), "Índice deve permanecer 0 após shuffle");
        assertTrue(player.isModoShuffle(), "Modo shuffle deve estar ativo");
        assertEquals(audioAntes, player.getAudioAtual(), "Áudio atual deve ser mantido");
    }

    @Test
    @DisplayName("Deve retornar null quando fila está vazia")
    void testFilaVazia() {
        Audio atual = player.getAudioAtual();

        assertNull(atual, "Áudio deve ser null com fila vazia");
        assertEquals(0, player.getTamanhoFila(), "Tamanho da fila deve ser 0");
    }

    @Test
    @DisplayName("Deve adicionar áudio à fila")
    void testAdicionarAFila() {
        Musica novaMusica = new Musica("Nova Música", 180, artistaTeste);

        player.adicionarAFila(novaMusica);

        assertEquals(1, player.getTamanhoFila(), "Fila deve ter 1 item");
        assertEquals(0, player.getIndiceAtual(), "Índice deve ser 0");
    }

    @Test
    @DisplayName("Deve ordenar fila por popularidade")
    void testOrdenarPorPopularidade() {
        // Simula curtidas
        Musica musica1 = new Musica("Música 1", 180, artistaTeste);
        Musica musica2 = new Musica("Música 2", 200, artistaTeste);
        musica2.curtir();
        musica2.curtir(); // 2 curtidas

        List<Audio> fila = new ArrayList<>();
        fila.add(musica1);
        fila.add(musica2);

        player.definirFila(fila);
        player.ordenarPorPopularidade();

        // Verifica que a fila foi ordenada (música com mais curtidas primeiro)
        List<Audio> filaOrdenada = player.getFila();
        assertEquals("Música 2", filaOrdenada.get(0).getTitulo(),
                "Música mais curtida deve estar primeiro na lista");
    }

    @Test
    @DisplayName("Deve limpar fila de reprodução")
    void testLimparFila() {
        player.definirFila(filaTeste);
        player.limparFila();

        assertEquals(0, player.getTamanhoFila(), "Fila deve estar vazia");
        assertEquals(-1, player.getIndiceAtual(), "Índice deve ser -1");
    }

    // ---------- Métodos Auxiliares ----------

    /**
     * Cria uma lista de músicas para testes.
     * 
     * @return Lista com 3 músicas de teste
     */
    private List<Audio> criarFilaTeste() {
        List<Audio> fila = new ArrayList<>();
        fila.add(new Musica("Música 1", 180, artistaTeste));
        fila.add(new Musica("Música 2", 200, artistaTeste));
        fila.add(new Musica("Música 3", 220, artistaTeste));
        return fila;
    }
}
