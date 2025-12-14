package test.service;

import model.usuario.Usuario;
import service.UsuarioService;
import exception.ValidacaoException;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

/**
 * Testes unitários para o UsuarioService usando JUnit 5.
 * Verifica funcionalidades de cadastro, login e validações.
 * 
 */
@DisplayName("UsuarioService Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioServiceTest {

    private static UsuarioService service;
    private static final String ARQUIVO_TESTE = "usuarios_teste.db";

    /**
     * Configura o ambiente de teste antes de todos os testes.
     * Usa instância singleton do UsuarioService.
     */
    @BeforeAll
    static void setUpAll() {
        service = UsuarioService.getInstance();
    }

    /**
     * Limpa arquivo de teste após todos os testes.
     */
    @AfterAll
    static void tearDownAll() {
        new File(ARQUIVO_TESTE).delete();
    }

    @Test
    @Order(1)
    @DisplayName("Deve cadastrar usuário com dados válidos")
    void testCadastroValido() {
        // Usa email único para evitar conflito com singleton
        String email = "teste_junit_" + System.currentTimeMillis() + "@email.com";
        Usuario user = service.cadastrar(email, "senha123", "Usuário Teste");

        assertNotNull(user, "Usuário não deve ser null");
        assertEquals(email, user.getEmail(), "Email deve corresponder");
    }

    @Test
    @Order(2)
    @DisplayName("Deve rejeitar cadastro com email duplicado")
    void testCadastroEmailExistente() {
        String email = "duplicado_" + System.currentTimeMillis() + "@email.com";
        service.cadastrar(email, "senha123", "Primeiro");

        // O serviço lança ValidacaoException para email duplicado
        assertThrows(ValidacaoException.class, () -> {
            service.cadastrar(email, "outrasenha", "Segundo");
        }, "Cadastro duplicado deve lançar ValidacaoException");
    }

    @Test
    @Order(3)
    @DisplayName("Deve rejeitar senha com menos de 4 caracteres")
    void testCadastroSenhaCurta() {
        String email = "curta_" + System.currentTimeMillis() + "@email.com";

        // O serviço lança ValidacaoException para senha curta
        assertThrows(ValidacaoException.class, () -> {
            service.cadastrar(email, "123", "Teste");
        }, "Senha curta deve lançar ValidacaoException");
    }

    @Test
    @Order(4)
    @DisplayName("Deve rejeitar email sem @ ou .")
    void testCadastroEmailInvalido() {
        // O serviço lança ValidacaoException para email inválido
        assertThrows(ValidacaoException.class, () -> {
            service.cadastrar("emailsemarroba", "senha123", "Teste");
        }, "Email inválido deve lançar ValidacaoException");
    }

    @Test
    @Order(5)
    @DisplayName("Deve fazer login com credenciais corretas")
    void testLoginValido() {
        String email = "login_" + System.currentTimeMillis() + "@email.com";
        service.cadastrar(email, "minhasenha", "Login Teste");

        Usuario logado = service.login(email, "minhasenha");

        assertNotNull(logado, "Login válido deve retornar usuário");
        assertEquals(email, logado.getEmail(), "Email deve corresponder");
    }

    @Test
    @Order(6)
    @DisplayName("Deve rejeitar login com senha incorreta")
    void testLoginSenhaIncorreta() {
        String email = "senhaerr_" + System.currentTimeMillis() + "@email.com";
        service.cadastrar(email, "correta1", "Teste");

        // O serviço lança ValidacaoException para senha incorreta
        assertThrows(ValidacaoException.class, () -> {
            service.login(email, "errada");
        }, "Senha incorreta deve lançar ValidacaoException");
    }

    @Test
    @Order(7)
    @DisplayName("Deve rejeitar login de usuário inexistente")
    void testLoginUsuarioInexistente() {
        // O serviço lança ValidacaoException para usuário inexistente
        assertThrows(ValidacaoException.class, () -> {
            service.login("naoexiste_absolutamente@email.com", "qualquer");
        }, "Usuário inexistente deve lançar ValidacaoException");
    }

    @Test
    @Order(8)
    @DisplayName("Deve verificar se usuário existe")
    void testExisteUsuario() {
        String email = "existe_" + System.currentTimeMillis() + "@email.com";
        service.cadastrar(email, "senha123", "Teste");

        assertTrue(service.existeUsuario(email), "Usuário cadastrado deve existir");
        assertFalse(service.existeUsuario("inexistente@xyz.com"), "Usuário não cadastrado não deve existir");
    }
}
