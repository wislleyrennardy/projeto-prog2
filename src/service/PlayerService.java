package service;

import model.midia.Audio;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Serviço responsável pelo controle de reprodução de áudio.
 * 
 * <p>
 * Esta classe gerencia a fila de reprodução e fornece controles
 * como play, pause, próximo, anterior, shuffle e ordenação.
 * </p>
 * 
 * <p>
 * Esta classe não realiza impressões na tela. A camada de menu
 * é responsável por exibir mensagens baseadas no estado do player.
 * </p>
 * 
 * <h2>Funcionalidades Principais</h2>
 * <ul>
 * <li>Gerenciamento de fila de reprodução (List&lt;Audio&gt;)</li>
 * <li>Controles de reprodução (play, pause, next, prev)</li>
 * <li>Embaralhamento (shuffle) e ordenação por popularidade</li>
 * <li>Adição individual ou em lote à fila</li>
 * </ul>
 * 
 * <h2>Uso de Coleções</h2>
 * <p>
 * Utiliza List&lt;Audio&gt; para a fila porque:
 * </p>
 * <ul>
 * <li>Mantém ordem de reprodução</li>
 * <li>Permite acesso por índice (próxima/anterior)</li>
 * <li>Suporta operações de shuffle e sort</li>
 * </ul>
 * 
 * @see Audio
 */
public class PlayerService {

    // ========= ATRIBUTOS =========

    /**
     * Lista que armazena os áudios na fila de reprodução.
     * Utiliza ArrayList para acesso rápido por índice O(1).
     */
    private List<Audio> filaReproducao;

    /**
     * Posição atual na fila de reprodução (0 a tamanho-1).
     * Valor -1 indica que nenhum item está selecionado.
     */
    private int indiceAtual;

    /**
     * Indica se há reprodução em andamento.
     * true = tocando, false = pausado ou parado.
     */
    private boolean tocando;

    /**
     * Indica se o modo shuffle está ativado.
     * Desativado automaticamente ao ordenar por popularidade.
     */
    private boolean modoShuffle;

    /**
     * Construtor que inicializa o player em estado "parado".
     * 
     * <p>
     * Cria uma fila vazia e define os estados iniciais:
     * índice em -1 (nenhum item), não tocando, shuffle desativado.
     * </p>
     */
    public PlayerService() {
        this.filaReproducao = new ArrayList<>();
        this.indiceAtual = -1;
        this.tocando = false;
        this.modoShuffle = false;
    }

    /**
     * Substitui toda a fila de reprodução por uma nova lista de áudios.
     * 
     * <p>
     * Este método é usado quando o usuário seleciona uma playlist
     * ou resultado de busca para tocar. A fila anterior é descartada.
     * </p>
     * 
     * @param novaFila Lista de áudios para definir como nova fila
     * @return true se a fila foi atualizada, false se a lista era vazia/nula
     */
    public boolean definirFila(List<Audio> novaFila) {
        // Validação: ignora listas nulas ou vazias
        if (novaFila == null || novaFila.isEmpty())
            return false;

        // Cria cópia defensiva para evitar modificações externas
        this.filaReproducao = new ArrayList<>(novaFila);
        this.indiceAtual = 0; // Posiciona no primeiro item
        this.tocando = false; // Aguarda comando de play
        return true;
    }

    /**
     * Inicia ou retoma a reprodução do áudio atual na fila.
     * 
     * <p>
     * Se o índice estiver inválido, automaticamente volta para
     * o primeiro item da fila. Incrementa o contador de reproduções
     * do áudio antes de reproduzi-lo.
     * </p>
     * 
     * @return true se iniciou reprodução, false se fila vazia
     */
    public boolean play() {
        // Verifica se há itens na fila
        if (filaReproducao.isEmpty()) {
            return false;
        }

        // Se índice inválido (ex: após limpar fila), reseta para início
        if (indiceAtual < 0 || indiceAtual >= filaReproducao.size()) {
            indiceAtual = 0;
        }

        // Obtém o áudio da posição atual e reproduz
        Audio atual = filaReproducao.get(indiceAtual);
        atual.incrementarReproducao(); // Incrementa estatística
        atual.reproduzir(); // Chama método polimórfico
        tocando = true; // Atualiza estado
        return true;
    }

    /**
     * Pausa a reprodução do áudio atual.
     * 
     * <p>
     * Só executa se houver algo tocando. Caso contrário, não faz nada.
     * </p>
     * 
     * @return true se pausou, false se não havia nada tocando
     */
    public boolean pause() {
        // Só pausa se realmente estiver tocando algo
        if (tocando && !filaReproducao.isEmpty()) {
            filaReproducao.get(indiceAtual).pausar();
            tocando = false;
            return true;
        }
        return false;
    }

    /**
     * Avança para a próxima faixa na fila de reprodução.
     * 
     * <p>
     * Se já estiver na última faixa, retorna false indicando
     * fim da playlist.
     * </p>
     * 
     * @return true se avançou, false se no fim da playlist ou fila vazia
     */
    public boolean proxima() {
        if (filaReproducao.isEmpty())
            return false;

        // Verifica se há próxima faixa disponível
        if (indiceAtual + 1 < filaReproducao.size()) {
            indiceAtual++; // Avança para próxima
            play(); // Inicia reprodução
            return true;
        } else {
            // Chegou ao fim da fila
            tocando = false;
            return false;
        }
    }

    /**
     * Volta para a faixa anterior na fila de reprodução.
     * 
     * <p>
     * Se já estiver na primeira faixa, reinicia a mesma
     * ao invés de fazer nada.
     * </p>
     * 
     * @return true se voltou para anterior, false se já estava no início
     */
    public boolean anterior() {
        if (filaReproducao.isEmpty())
            return false;

        // Verifica se há faixa anterior disponível
        if (indiceAtual - 1 >= 0) {
            indiceAtual--; // Volta uma posição
            play(); // Inicia reprodução
            return true;
        } else {
            // Já está no início, reinicia a faixa atual
            play();
            return false;
        }
    }

    /**
     * Embaralha a fila de reprodução aleatoriamente (modo shuffle).
     * 
     * <p>
     * Usa {@link Collections#shuffle} para randomizar a ordem.
     * O áudio que estava tocando é mantido na primeira posição
     * para não interromper a reprodução atual.
     * </p>
     * 
     * @return true se embaralhou, false se fila vazia
     */
    public boolean shuffle() {
        if (filaReproducao.isEmpty()) {
            return false;
        }

        // Salva o áudio atual para manter na posição 0 após shuffle
        Audio audioAtual = getAudioAtual();

        // Embaralha toda a fila usando algoritmo Fisher-Yates
        Collections.shuffle(filaReproducao);

        // Move o áudio que estava tocando para primeira posição
        if (audioAtual != null) {
            filaReproducao.remove(audioAtual);
            filaReproducao.add(0, audioAtual);
            indiceAtual = 0;
        }

        modoShuffle = true;
        return true;
    }

    /**
     * Ordena a fila por popularidade (número de curtidas).
     * 
     * <p>
     * Os áudios mais curtidos ficam no início da fila.
     * Usa {@link Collections#sort} com um {@link Comparator}
     * customizado para ordenar de forma decrescente.
     * </p>
     * 
     * <p>
     * Desativa o modo shuffle automaticamente.
     * </p>
     * 
     * @return true se ordenou, false se fila vazia
     */
    public boolean ordenarPorPopularidade() {
        if (filaReproducao.isEmpty()) {
            return false;
        }

        // Guarda referência do áudio atual para reposicionar depois
        Audio audioAtual = getAudioAtual();

        // Ordena usando Comparator anônimo (ordem decrescente de curtidas)
        Collections.sort(filaReproducao, new Comparator<Audio>() {
            @Override
            public int compare(Audio o1, Audio o2) {
                // o2 primeiro para ordem decrescente
                return Integer.compare(o2.getTotalCurtidas(), o1.getTotalCurtidas());
            }
        });

        // Reposiciona índice para o áudio que estava tocando
        if (audioAtual != null) {
            indiceAtual = filaReproducao.indexOf(audioAtual);
            if (indiceAtual < 0)
                indiceAtual = 0;
        }

        modoShuffle = false; // Desativa shuffle
        return true;
    }

    /**
     * Retorna o áudio na posição atual da fila.
     * 
     * @return O áudio atual, ou null se a fila estiver vazia
     */
    public Audio getAudioAtual() {
        if (indiceAtual >= 0 && indiceAtual < filaReproducao.size()) {
            return filaReproducao.get(indiceAtual);
        }
        return null;
    }

    /**
     * Retorna o índice da posição atual na fila (base 0).
     * 
     * @return Índice atual, ou -1 se nenhum item selecionado
     */
    public int getIndiceAtual() {
        return indiceAtual;
    }

    /**
     * Define manualmente a posição atual na fila.
     * 
     * <p>
     * Usado para restaurar estado salvo do player.
     * </p>
     * 
     * @param indice Nova posição (deve ser válida)
     */
    public void setIndiceAtual(int indice) {
        if (indice >= 0 && indice < filaReproducao.size()) {
            this.indiceAtual = indice;
        }
    }

    /**
     * Verifica se há reprodução em andamento.
     * 
     * @return true se estiver tocando, false caso contrário
     */
    public boolean isTocando() {
        return tocando;
    }

    /**
     * Verifica se o modo shuffle está ativado.
     * 
     * @return true se shuffle ativo, false caso contrário
     */
    public boolean isModoShuffle() {
        return modoShuffle;
    }

    /**
     * Retorna o número total de itens na fila.
     * 
     * @return Quantidade de áudios na fila
     */
    public int getTamanhoFila() {
        return filaReproducao.size();
    }

    /**
     * Retorna o status atual do player.
     * 
     * @return String com status formatado
     */
    public String getStatus() {
        if (filaReproducao.isEmpty()) {
            return "Fila vazia";
        }
        return tocando ? "▶ Reproduzindo" : "⏸ Pausado";
    }

    /**
     * Adiciona um áudio ao final da fila de reprodução.
     * 
     * @param audio Áudio a adicionar
     * @return true se adicionou, false se áudio era null
     */
    public boolean adicionarAFila(Audio audio) {
        if (audio == null)
            return false;

        filaReproducao.add(audio);

        // Se fila estava vazia, posiciona no primeiro item
        if (filaReproducao.size() == 1) {
            indiceAtual = 0;
        }
        return true;
    }

    /**
     * Adiciona múltiplos áudios à fila.
     * 
     * @param audios     Lista de áudios a adicionar
     * @param nomeOrigem Nome da origem (para referência, não usado internamente)
     * @return Número de itens adicionados
     */
    public int adicionarListaAFila(List<Audio> audios, String nomeOrigem) {
        if (audios == null || audios.isEmpty())
            return 0;

        boolean filaVazia = filaReproducao.isEmpty();
        filaReproducao.addAll(audios);

        if (filaVazia) {
            indiceAtual = 0;
        }

        return audios.size();
    }

    /**
     * Retorna a fila de reprodução para visualização.
     * 
     * @return Cópia da fila de reprodução
     */
    public List<Audio> getFila() {
        return new ArrayList<>(filaReproducao);
    }

    /**
     * Limpa a fila de reprodução.
     */
    public void limparFila() {
        filaReproducao.clear();
        indiceAtual = -1;
        tocando = false;
    }
}
