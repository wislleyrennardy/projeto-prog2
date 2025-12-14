package model.usuario;

import model.midia.Audio;
import model.playlist.Playlist;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Representa um usuário do sistema AudioStreaming.
 * 
 * <p>
 * Esta classe gerencia autenticação, playlists pessoais e curtidas.
 * Implementa {@link Serializable} para permitir persistência em disco.
 * </p>
 * 
 * <h2>Segurança</h2>
 * <p>
 * A senha é armazenada como hash SHA-256, nunca em texto plano.
 * Isso garante que mesmo se os dados forem acessados, a senha
 * original não pode ser recuperada.
 * </p>
 * 
 * <h2>Uso de Coleções</h2>
 * <ul>
 * <li>List&lt;Playlist&gt; - Mantém ordem das playlists criadas</li>
 * <li>Set&lt;Audio&gt; - Evita curtidas duplicadas, busca O(1)</li>
 * </ul>
 * 
 * @see Playlist
 */
public class Usuario implements Serializable {

	/** Identificador de versão para serialização. */
	private static final long serialVersionUID = 3L;

	/** Identificador único do usuário (UUID). */
	private String id;

	/** Email do usuário (usado para login). */
	private String email;

	/** Hash SHA-256 da senha (nunca texto plano). */
	private String senhaHash;

	/** Nome de exibição do usuário. */
	private String nome;

	/** Lista de playlists pessoais do usuário. */
	private List<Playlist> playlists;

	/** Conjunto de áudios curtidos (sem duplicatas). */
	private Set<Audio> curtidas;

	/** Índice salvo da última posição no player. */
	private int estadoPlayerIndice;

	/**
	 * Cria um novo usuário com autenticação.
	 * 
	 * <p>
	 * A senha é automaticamente convertida em hash SHA-256.
	 * O email é normalizado (lowercase, sem espaços).
	 * </p>
	 * 
	 * @param email Email do usuário (usado para login)
	 * @param senha Senha em texto plano (será hasheada)
	 * @param nome  Nome de exibição
	 */
	public Usuario(String email, String senha, String nome) {
		this.id = UUID.randomUUID().toString();
		this.email = email.toLowerCase().trim(); // Normaliza email
		this.senhaHash = hashSenha(senha); // Armazena apenas hash
		this.nome = nome;
		this.playlists = new ArrayList<>(); // Lista ordenada
		this.curtidas = new HashSet<>(); // Set para evitar duplicatas
		this.estadoPlayerIndice = -1; // Nenhum estado salvo
	}

	/**
	 * Gera hash SHA-256 de uma senha.
	 * 
	 * <p>
	 * Este método converte uma senha em texto plano para um hash
	 * de 64 caracteres hexadecimais. O mesmo texto sempre gera
	 * o mesmo hash, permitindo verificação de senhas.
	 * </p>
	 * 
	 * @param senha Senha em texto plano
	 * @return Hash SHA-256 em formato hexadecimal
	 * @throws RuntimeException Se algoritmo SHA-256 não estiver disponível
	 */
	private static String hashSenha(String senha) {
		try {
			// Obtém instância do algoritmo SHA-256
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			// Calcula hash dos bytes da senha
			byte[] hash = digest.digest(senha.getBytes(StandardCharsets.UTF_8));

			// Converte bytes para hexadecimal
			StringBuilder hexString = new StringBuilder();
			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1)
					hexString.append('0'); // Padding para 2 dígitos
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Erro ao gerar hash da senha", e);
		}
	}

	/**
	 * Verifica se uma senha corresponde à senha do usuário.
	 * 
	 * <p>
	 * Gera o hash da senha fornecida e compara com o hash armazenado.
	 * </p>
	 * 
	 * @param senha Senha em texto plano para verificar
	 * @return true se a senha está correta, false caso contrário
	 */
	public boolean verificarSenha(String senha) {
		return this.senhaHash.equals(hashSenha(senha));
	}

	/**
	 * Altera a senha do usuário.
	 * 
	 * @param novaSenha Nova senha em texto plano (será hasheada)
	 */
	public void alterarSenha(String novaSenha) {
		this.senhaHash = hashSenha(novaSenha);
	}

	/**
	 * Cria uma nova playlist para o usuário.
	 * 
	 * @param nomePlaylist Nome da nova playlist
	 */
	public void criarPlaylist(String nomePlaylist) {
		Playlist p = new Playlist(nomePlaylist);
		playlists.add(p);
	}

	/**
	 * Alterna o estado de curtida de um áudio (toggle).
	 * 
	 * <p>
	 * Se o áudio já está curtido, remove a curtida.
	 * Se não está curtido, adiciona a curtida.
	 * Atualiza também o contador global de curtidas do áudio.
	 * </p>
	 * 
	 * @param audio Áudio para curtir/descurtir
	 * @return true se a curtida foi adicionada, false se foi removida
	 */
	public boolean curtirAudio(Audio audio) {
		// Verifica se já curtiu usando Set (busca O(1))
		if (curtidas.contains(audio)) {
			// Remove curtida existente
			curtidas.remove(audio);
			audio.descurtir(); // Decrementa contador global
			return false; // Indica que a curtida foi removida
		} else {
			// Adiciona nova curtida
			curtidas.add(audio);
			audio.curtir(); // Incrementa contador global
			return true; // Indica que a curtida foi adicionada
		}
	}

	// ========= GETTERS =========

	/**
	 * Retorna o identificador único do usuário.
	 * 
	 * @return UUID do usuário
	 */
	public String getId() {
		return id;
	}

	/**
	 * Retorna o email do usuário.
	 * 
	 * @return Email normalizado
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Retorna a lista de playlists do usuário.
	 * 
	 * @return Lista de playlists
	 */
	public List<Playlist> getPlaylists() {
		return playlists;
	}

	/**
	 * Retorna o conjunto de áudios curtidos.
	 * 
	 * @return Set de curtidas
	 */
	public Set<Audio> getCurtidas() {
		return curtidas;
	}

	/**
	 * Retorna o nome de exibição do usuário.
	 * 
	 * @return Nome do usuário
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Retorna o índice salvo do player.
	 * 
	 * @return Índice da última posição, ou -1 se não há estado salvo
	 */
	public int getEstadoPlayerIndice() {
		return estadoPlayerIndice;
	}

	/**
	 * Salva a posição atual do player para restaurar depois.
	 * 
	 * @param indice Índice para salvar
	 */
	public void setEstadoPlayerIndice(int indice) {
		this.estadoPlayerIndice = indice;
	}

	/**
	 * Compara usuários pela igualdade de ID.
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
		Usuario usuario = (Usuario) o;
		return Objects.equals(id, usuario.id);
	}

	/**
	 * Gera hash code baseado no ID.
	 * 
	 * @return Hash code
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	/**
	 * Retorna representação textual do usuário.
	 * 
	 * @return String no formato "Nome &lt;email&gt;"
	 */
	@Override
	public String toString() {
		return nome + " <" + email + ">";
	}
}
