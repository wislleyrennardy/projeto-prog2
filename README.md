# AudioStreaming 🎵

**Projeto final - Disciplina de Programação 2**  
Curso Superior de Tecnologia em Telemática - IFPB

## Descrição

Sistema de gerenciamento de músicas, playlists, artistas e reprodução simulada. Implementa conceitos de Programação Orientada a Objetos em Java, incluindo herança, interfaces, coleções e persistência de dados.

## Funcionalidades

- 🔐 **Sistema Multiusuário**: Login e cadastro de usuários com senha criptografada (SHA-256)
- 🎵 **Catálogo de Músicas**: Biblioteca com músicas organizadas por artista e álbum
- 🎙️ **Podcasts**: Suporte a episódios de podcasts
- 📂 **Playlists**: Criação, edição e remoção de playlists personalizadas
- ❤️ **Curtidas**: Sistema de curtidas por usuário com toggle (curtir/descurtir)
- 🔀 **Shuffle**: Embaralhar fila de reprodução
- 📊 **Ordenação por Popularidade**: Ordenar músicas por número de curtidas
- 🔥 **Recomendações**: Top Charts baseado nas músicas mais curtidas
- 💾 **Persistência**: Dados salvos automaticamente ao sair

## Estrutura de Pacotes

```
src/
├── main/
│   └── Projeto.java              # Classe principal (CLI)
├── model/
│   ├── midia/
│   │   ├── Audio.java            # Classe abstrata base
│   │   ├── Artista.java          # Modelo de artista
│   │   ├── Album.java            # Modelo de álbum
│   │   ├── Musica.java           # Classe de música
│   │   └── Podcast.java          # Classe de podcast
│   ├── playlist/
│   │   └── Playlist.java         # Gerenciamento de playlists
│   └── usuario/
│       └── Usuario.java          # Modelo de usuário com autenticação
├── service/
│   ├── BibliotecaService.java    # Gerenciamento do catálogo (Singleton)
│   ├── PlayerService.java        # Controle de reprodução
│   └── UsuarioService.java       # Gerenciamento de usuários (Singleton)
├── interfaces/
│   └── Reproduzivel.java         # Interface para itens reproduzíveis
├── exception/
│   ├── BaseAppException.java     # Exceção base da aplicação
│   ├── ValidacaoException.java   # Exceções de validação
│   └── UsuarioNaoEncontradoException.java
├── menu/
│   ├── MenuPrincipal.java        # Menu principal do sistema
│   ├── MenuPlayer.java           # Controles do player
│   ├── MenuPlaylist.java         # Gerenciamento de playlists
│   ├── TelaAutenticacao.java     # Login e cadastro
│   ├── TelaCatalogo.java         # Navegação pelo catálogo
│   ├── TelaPerfil.java           # Perfil do usuário
│   └── UtilConsole.java          # Utilitários de console
└── test/
    ├── service/
    │   ├── PlayerServiceTest.java
    │   ├── UsuarioServiceTest.java
    │   └── BibliotecaServiceTest.java
    └── model/playlist/
        └── PlaylistTest.java
```

## Documentação

📖 **Javadoc**: A documentação completa das classes e métodos está disponível online:
- [Visualizar Javadoc](https://htmlpreview.github.io/?https://github.com/wislleyrennardy/projeto-prog2/blob/implementacao/docs/javadoc/index.html)

📐 **Diagrama de Classes**: Visualização UML da estrutura do projeto:
- [Ver Diagrama de Classes](DIAGRAMA_CLASSES.md)

## Compilação e Execução

### Pré-requisitos
- Java JDK 11 ou superior

> [!TIP]
> Para melhor suporte a emojis no Windows, recomendamos usar o **Windows Terminal** com a fonte **Cascadia Code**.

### Scripts de Execução (Recomendado)

O projeto inclui scripts prontos para compilar, executar e gerar JAR.

| Ação | Windows | Linux/Mac |
|------|---------|-----------|
| Compilar | `scripts\windows\compilar.bat` | `./scripts/unix/compilar.sh` |
| Executar | `scripts\windows\executar.bat` | `./scripts/unix/executar.sh` |
| Executar JAR | `scripts\windows\executar-jar.bat` | `./scripts/unix/executar-jar.sh` |
| Gerar JAR | `scripts\windows\gerar-jar.bat` | `./scripts/unix/gerar-jar.sh` |
| Compilar Testes | `scripts\windows\compilar-testes.bat` | `./scripts/unix/compilar-testes.sh` |
| Executar Testes | `scripts\windows\executar-testes.bat` | `./scripts/unix/executar-testes.sh` |

**Linux/Mac:** Torne os scripts executáveis primeiro:
```bash
chmod +x scripts/unix/*.sh
```

### Comandos Manuais

#### Compilar
```bash
# Windows (CMD)
javac -encoding UTF-8 -d bin -sourcepath src src/main/Projeto.java

# Linux/Mac
javac -encoding UTF-8 -d bin -sourcepath src src/main/Projeto.java
```

#### Executar (com suporte a emojis)
```bash
# Windows (CMD) - Execute estes comandos em sequência
chcp 65001
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp bin main.Projeto

# Linux/Mac
export LANG=pt_BR.UTF-8 && java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp bin main.Projeto
```

#### Gerar JAR Executável
```bash
# Compilar
javac -encoding UTF-8 -d bin -sourcepath src src/main/Projeto.java

# Criar JAR
cd bin
jar cfe ../AudioStreaming.jar main.Projeto .
cd ..

# Executar JAR (com suporte a emojis)
# Windows (CMD)
chcp 65001
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -jar AudioStreaming.jar

# Linux/Mac
export LANG=pt_BR.UTF-8 && java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -jar AudioStreaming.jar
```

## Executar Testes JUnit

O projeto inclui testes unitários usando JUnit 5. Para executá-los:

#### Compilar com Testes
```bash
# Compilar todos os arquivos incluindo testes
javac -encoding UTF-8 -d bin -cp "lib/*" -sourcepath src src/main/Projeto.java src/test/*.java
```

#### Executar Testes
```bash
# Executar todos os testes
java -Dfile.encoding=UTF-8 -jar lib/junit-platform-console-standalone-1.10.2.jar --class-path bin --scan-classpath

# Executar teste específico
java -Dfile.encoding=UTF-8 -jar lib/junit-platform-console-standalone-1.10.2.jar --class-path bin --select-class test.PlayerServiceTest
```

### Testes Disponíveis
| Pacote | Classe | Cobertura |
|--------|--------|-----------|
| `test.service` | `PlayerServiceTest` | Fila, play, pause, shuffle, navegação |
| `test.service` | `UsuarioServiceTest` | Cadastro, login, validações |
| `test.service` | `BibliotecaServiceTest` | Busca, indexação, recomendações |
| `test.model.playlist` | `PlaylistTest` | CRUD, prevenção de duplicatas |

## Como Usar

### Primeiro Acesso
1. Execute o programa
2. Selecione a opção **2. 📝 Cadastrar**
3. Preencha email (para login), nome de exibição e senha
4. Após cadastro, você será logado automaticamente

### Login
1. Selecione a opção **1. 🔑 Login**
2. Digite seu email e senha

### Menu Principal
- **1. 🎵 Catálogo Completo**: Ver todas as músicas e podcasts
- **2. 📊 Recomendações (Top Charts)**: Ver músicas mais populares
- **3. 🔍 Buscar Música/Artista**: Pesquisar por título ou artista
- **4. 📂 Minhas Playlists**: Gerenciar suas playlists pessoais
- **5. ⏯️ Player (Controles)**: Controles de reprodução (play, pause, next, shuffle, ordenar)
- **6. ❤️ Meu Perfil (Curtidas)**: Ver suas curtidas e configurações da conta
- **0. 💾 Sair e Salvar**: Salvar dados e encerrar

## Arquivos de Dados

- `catalogo.db` - Catálogo de músicas e podcasts
- `usuarios.db` - Dados dos usuários cadastrados

## Tecnologias

- **Linguagem**: Java 11+
- **Persistência**: Serialização Java
- **Interface**: Console (CLI)
- **Criptografia**: SHA-256 para senhas

## Licença

Projeto acadêmico - Uso educacional
