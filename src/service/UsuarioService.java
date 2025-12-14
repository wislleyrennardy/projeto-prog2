package service;

import exception.ValidacaoException;
import model.usuario.Usuario;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Serviço responsável pelo gerenciamento de usuários.
 * Implementa Singleton para acesso global.
 * Utiliza Map para busca rápida por email.
 * 
 * <p>
 * Esta classe não realiza impressões na tela. Erros são comunicados
 * via exceções e a camada de menu é responsável pela exibição.
 * </p>
 */
public class UsuarioService {
    private static UsuarioService instance;
    private static final String ARQUIVO_USUARIOS = "usuarios.db";

    // Map: email -> Usuario (busca O(1))
    private Map<String, Usuario> usuarios;

    private UsuarioService() {
        this.usuarios = new HashMap<>();
    }

    public static UsuarioService getInstance() {
        if (instance == null) {
            instance = new UsuarioService();
        }
        return instance;
    }

    /**
     * Cadastra um novo usuário no sistema.
     * 
     * @param email Email do usuário (único)
     * @param senha Senha em texto plano (será hasheada)
     * @param nome  Nome de exibição
     * @return O usuário cadastrado
     * @throws ValidacaoException Se dados inválidos ou email já existente
     */
    public Usuario cadastrar(String email, String senha, String nome) throws ValidacaoException {
        // Validação de campos obrigatórios
        if (email == null || email.trim().isEmpty() || senha == null || senha.isEmpty() || nome == null
                || nome.trim().isEmpty()) {
            throw new ValidacaoException("Todos os campos são obrigatórios.");
        }

        String emailNormalizado = email.toLowerCase().trim();

        // Validação de formato de email
        if (!emailNormalizado.contains("@") || !emailNormalizado.contains(".")) {
            throw new ValidacaoException("Formato de email inválido.");
        }

        // Validação de email único
        if (existeUsuario(emailNormalizado)) {
            throw new ValidacaoException("Email '" + email + "' já está cadastrado.");
        }

        // Validação de tamanho de senha
        if (senha.length() < 4) {
            throw new ValidacaoException("A senha deve ter pelo menos 4 caracteres.");
        }

        // Criação do usuário
        Usuario novoUsuario = new Usuario(emailNormalizado, senha, nome.trim());
        usuarios.put(emailNormalizado, novoUsuario);
        salvarUsuarios();

        return novoUsuario;
    }

    /**
     * Realiza login do usuário.
     * 
     * @param email Email do usuário
     * @param senha Senha em texto plano
     * @return O usuário logado
     * @throws ValidacaoException Se credenciais inválidas
     */
    public Usuario login(String email, String senha) throws ValidacaoException {
        String emailNormalizado = email.toLowerCase().trim();

        Usuario usuario = usuarios.get(emailNormalizado);

        if (usuario == null) {
            throw new ValidacaoException("Usuário não encontrado.");
        }

        if (!usuario.verificarSenha(senha)) {
            throw new ValidacaoException("Senha incorreta.");
        }

        return usuario;
    }

    /**
     * Verifica se um email já está em uso.
     * 
     * @param email Email a verificar
     * @return true se já existe, false caso contrário
     */
    public boolean existeUsuario(String email) {
        return usuarios.containsKey(email.toLowerCase().trim());
    }

    /**
     * Remove um usuário do sistema.
     * 
     * @param email Email do usuário a remover
     */
    public void removerUsuario(String email) {
        String emailNormalizado = email.toLowerCase().trim();
        usuarios.remove(emailNormalizado);
        salvarUsuarios();
    }

    /**
     * Retorna o número total de usuários cadastrados.
     * 
     * @return Quantidade de usuários
     */
    public int getTotalUsuarios() {
        return usuarios.size();
    }

    /**
     * Salva usuários no disco.
     * Operação silenciosa - não imprime mensagens.
     */
    public void salvarUsuarios() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_USUARIOS))) {
            oos.writeObject(usuarios);
        } catch (IOException e) {
            // Erro silencioso - log poderia ser adicionado aqui
        }
    }

    /**
     * Carrega usuários do disco.
     * Operação silenciosa - não imprime mensagens.
     * 
     * @return Número de usuários carregados
     */
    @SuppressWarnings("unchecked")
    public int carregarUsuarios() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_USUARIOS))) {
            this.usuarios = (Map<String, Usuario>) ois.readObject();
            return usuarios.size();
        } catch (FileNotFoundException e) {
            // Arquivo não existe - sistema iniciado vazio
            this.usuarios = new HashMap<>();
            return 0;
        } catch (IOException | ClassNotFoundException e) {
            // Erro ao ler - reinicia com mapa vazio
            this.usuarios = new HashMap<>();
            return -1; // Indica erro
        }
    }
}
