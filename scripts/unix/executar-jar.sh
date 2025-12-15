#!/bin/bash
# ============================================
# Executar AudioStreaming via JAR (Linux/Unix/macOS)
# Configura UTF-8 para exibir emojis corretamente
# ============================================

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR/../.."

export LANG=pt_BR.UTF-8
export LC_ALL=pt_BR.UTF-8

# Fallback para en_US.UTF-8 se pt_BR não estiver disponível
if ! locale -a 2>/dev/null | grep -qi "pt_BR"; then
    export LANG=en_US.UTF-8
    export LC_ALL=en_US.UTF-8
fi

java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -jar AudioStreaming.jar
