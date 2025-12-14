#!/bin/bash
# ============================================
# Compilar AudioStreaming (Linux/Unix/macOS)
# ============================================

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR/../.."

echo "Compilando AudioStreaming..."
javac -encoding UTF-8 -d bin -sourcepath src src/main/Projeto.java

if [ $? -eq 0 ]; then
    echo "Compilação concluída com sucesso!"
else
    echo "Erro na compilação."
    exit 1
fi
