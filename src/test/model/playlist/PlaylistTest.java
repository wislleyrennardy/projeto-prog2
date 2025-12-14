package test.model.playlist;

import model.midia.*;
import model.playlist.Playlist;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe Playlist usando JUnit 5.
 * Verifica funcionalidades de adição, remoção e prevenção de duplicatas.
 * 
 */
@DisplayName("Playlist Tests")
public class PlaylistTest {

    private Playlist playlist;
    private Artista artistaTeste;

    @BeforeEach
    void setUp() {
        playlist = new Playlist("Minha Playlist");
        artistaTeste = new Artista("Artista Teste");
    }

    @Test
    @DisplayName("Deve criar playlist com nome correto")
    void testCriarPlaylist() {
        assertEquals("Minha Playlist", playlist.getNome(), "Nome deve corresponder");
        assertTrue(playlist.getItens().isEmpty(), "Playlist nova deve estar vazia");
    }

    @Test
    @DisplayName("Deve adicionar item à playlist")
    void testAdicionarItem() {
        Musica musica = new Musica("Teste", 180, artistaTeste);

        boolean adicionado = playlist.adicionarItem(musica);

        assertTrue(adicionado, "Deve retornar true ao adicionar");
        assertEquals(1, playlist.getItens().size(), "Playlist deve ter 1 item");
    }

    @Test
    @DisplayName("Deve rejeitar item duplicado")
    void testRejeitarDuplicata() {
        Musica musica = new Musica("Teste", 180, artistaTeste);

        playlist.adicionarItem(musica);
        boolean segundaAdicao = playlist.adicionarItem(musica);

        assertFalse(segundaAdicao, "Deve retornar false para duplicata");
        assertEquals(1, playlist.getItens().size(), "Playlist deve continuar com 1 item");
    }

    @Test
    @DisplayName("Deve remover item da playlist")
    void testRemoverItem() {
        Musica musica = new Musica("Teste", 180, artistaTeste);
        playlist.adicionarItem(musica);

        playlist.removerItem(musica);

        assertTrue(playlist.getItens().isEmpty(), "Playlist deve estar vazia após remoção");
    }

    @Test
    @DisplayName("Deve manter múltiplos itens diferentes")
    void testMultiplosItens() {
        Musica musica1 = new Musica("Música 1", 180, artistaTeste);
        Musica musica2 = new Musica("Música 2", 200, artistaTeste);
        Podcast podcast = new Podcast("Podcast", 3600, "Host", 1);

        playlist.adicionarItem(musica1);
        playlist.adicionarItem(musica2);
        playlist.adicionarItem(podcast);

        assertEquals(3, playlist.getItens().size(), "Playlist deve ter 3 itens");
    }

    @Test
    @DisplayName("toString deve mostrar nome e quantidade de itens")
    void testToString() {
        Musica musica = new Musica("Teste", 180, artistaTeste);
        playlist.adicionarItem(musica);

        String str = playlist.toString();

        assertTrue(str.contains("Minha Playlist"), "Deve conter nome");
        assertTrue(str.contains("1 itens"), "Deve conter quantidade");
    }
}
