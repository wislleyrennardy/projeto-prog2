package test.service;

import model.midia.*;
import service.BibliotecaService;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Testes unitários para o BibliotecaService usando JUnit 5.
 * Verifica funcionalidades de catálogo, busca e recomendações.
 * 
 */
@DisplayName("BibliotecaService Tests")
public class BibliotecaServiceTest {

    private BibliotecaService biblioteca;
    private static boolean dadosAdicionados = false;

    @BeforeEach
    void setUp() {
        biblioteca = BibliotecaService.getInstance();

        // Adiciona dados de teste apenas uma vez (singleton)
        if (!dadosAdicionados) {
            Artista queen = biblioteca.getOuCriarArtista("Queen");
            biblioteca.adicionarAudio(new Musica("Bohemian Rhapsody", 354, queen));
            biblioteca.adicionarAudio(new Musica("We Will Rock You", 200, queen));

            Artista edSheeran = biblioteca.getOuCriarArtista("Ed Sheeran");
            biblioteca.adicionarAudio(new Musica("Shape of You", 233, edSheeran));

            dadosAdicionados = true;
        }
    }

    @Test
    @DisplayName("Deve retornar instância singleton")
    void testSingleton() {
        BibliotecaService outraInstancia = BibliotecaService.getInstance();

        assertSame(biblioteca, outraInstancia, "Deve retornar mesma instância");
    }

    @Test
    @DisplayName("Deve obter ou criar artista por nome")
    void testGetOuCriarArtista() {
        Artista artista1 = biblioteca.getOuCriarArtista("Artista Novo Teste");
        Artista artista2 = biblioteca.getOuCriarArtista("Artista Novo Teste");
        Artista artista3 = biblioteca.getOuCriarArtista("ARTISTA NOVO TESTE"); // case insensitive

        assertNotNull(artista1, "Artista não deve ser null");
        assertSame(artista1, artista2, "Deve retornar mesmo artista");
        assertSame(artista1, artista3, "Busca deve ser case insensitive");
    }

    @Test
    @DisplayName("Deve buscar por título")
    void testBuscaPorTitulo() {
        List<Audio> resultados = biblioteca.buscar("Bohemian");

        assertFalse(resultados.isEmpty(), "Deve encontrar resultados");
        assertTrue(resultados.stream()
                .anyMatch(a -> a.getTitulo().contains("Bohemian")),
                "Deve conter música buscada");
    }

    @Test
    @DisplayName("Deve buscar por artista")
    void testBuscaPorArtista() {
        List<Audio> resultados = biblioteca.buscar("Queen");

        assertFalse(resultados.isEmpty(), "Deve encontrar músicas do artista");
    }

    @Test
    @DisplayName("Deve retornar lista vazia para busca sem resultados")
    void testBuscaSemResultados() {
        List<Audio> resultados = biblioteca.buscar("xyz123naoexiste456");

        assertTrue(resultados.isEmpty(), "Busca sem resultados deve retornar lista vazia");
    }

    @Test
    @DisplayName("Deve retornar catálogo ordenado por popularidade")
    void testOrdenarPorPopularidade() {
        // Adiciona curtidas a uma música específica
        List<Audio> catalogo = biblioteca.getCatalogo();
        if (!catalogo.isEmpty()) {
            catalogo.get(0).curtir();
            catalogo.get(0).curtir();
        }

        List<Audio> ordenado = biblioteca.getCatalogoPorPopularidade();

        assertFalse(ordenado.isEmpty(), "Catálogo não deve estar vazio");

        // Verifica ordenação (primeiro deve ter mais ou igual curtidas que segundo)
        if (ordenado.size() >= 2) {
            assertTrue(
                    ordenado.get(0).getTotalCurtidas() >= ordenado.get(1).getTotalCurtidas(),
                    "Primeiro item deve ter igual ou mais curtidas que segundo");
        }
    }

    @Test
    @DisplayName("Deve retornar top 5 recomendações")
    void testRecomendarMaisCurtidos() {
        List<Audio> recomendacoes = biblioteca.recomendarMaisCurtidos();

        assertNotNull(recomendacoes, "Recomendações não devem ser null");
        assertTrue(recomendacoes.size() <= 5, "Deve ter no máximo 5 recomendações");
    }

    @Test
    @DisplayName("Catálogo deve conter músicas após adição")
    void testCatalogoContemMusicas() {
        List<Audio> catalogo = biblioteca.getCatalogo();

        assertNotNull(catalogo, "Catálogo não deve ser null");
        assertFalse(catalogo.isEmpty(), "Catálogo deve conter músicas");
    }
}
