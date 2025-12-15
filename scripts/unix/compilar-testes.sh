#!/bin/bash
# ============================================
# Compilar Testes JUnit (Linux/Unix/macOS)
# ============================================

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR/../.."

echo "Compilando código e testes..."

# Encontra todos os arquivos .java de teste recursivamente
TEST_FILES=$(find src/test -name "*.java" 2>/dev/null)

if [ -z "$TEST_FILES" ]; then
    echo "Nenhum arquivo de teste encontrado em src/test"
    exit 1
fi

# Compila o codigo principal e os testes
javac -encoding UTF-8 -d bin -cp "lib/*" -sourcepath src src/main/Projeto.java $TEST_FILES

if [ $? -eq 0 ]; then
    echo "Compilação concluída com sucesso!"
else
    echo "Erro na compilação."
    exit 1
fi
