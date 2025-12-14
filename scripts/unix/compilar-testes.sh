#!/bin/bash
# ============================================
# Compilar Testes JUnit (Linux/Unix/macOS)
# ============================================

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR/../.."

echo "Compilando código e testes..."
javac -encoding UTF-8 -d bin -cp "lib/*" -sourcepath src src/main/Projeto.java src/test/*.java

if [ $? -eq 0 ]; then
    echo "Compilação concluída com sucesso!"
else
    echo "Erro na compilação."
    exit 1
fi
