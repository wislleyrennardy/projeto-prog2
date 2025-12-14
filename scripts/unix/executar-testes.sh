#!/bin/bash
# ============================================
# Executar Testes JUnit (Linux/Unix/macOS)
# ============================================

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR/../.."

export LANG=pt_BR.UTF-8
export LC_ALL=pt_BR.UTF-8

echo "Executando todos os testes..."
java -Dfile.encoding=UTF-8 -jar lib/junit-platform-console-standalone-1.10.2.jar --class-path bin --scan-classpath
