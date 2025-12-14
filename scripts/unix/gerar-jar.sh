#!/bin/bash
# ============================================
# Gerar JAR Executável AudioStreaming (Linux/Unix/macOS)
# ============================================

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR/../.."

echo "Compilando..."
javac -encoding UTF-8 -d bin -sourcepath src src/main/Projeto.java

if [ $? -ne 0 ]; then
    echo "Erro na compilação."
    exit 1
fi

echo "Criando JAR..."
cd bin
jar cfe ../AudioStreaming.jar main.Projeto .
cd ..

if [ $? -eq 0 ]; then
    echo "JAR criado com sucesso: AudioStreaming.jar"
else
    echo "Erro ao criar JAR."
    exit 1
fi
