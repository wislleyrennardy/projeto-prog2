#!/bin/bash

echo "═══════════════════════════════════════════"
echo "       📚 Gerando Javadoc"
echo "═══════════════════════════════════════════"
echo

# Navega para o diretório raiz do projeto
cd "$(dirname "$0")/../.."

if [ -d "docs/javadoc" ]; then
    echo "Removendo Javadoc antigo..."
    rm -rf docs/javadoc
fi

echo "Gerando nova documentação..."
javadoc -encoding UTF-8 -charset UTF-8 -docencoding UTF-8 \
    -d docs/javadoc \
    -subpackages main:model:service:menu:exception:interfaces \
    -sourcepath src \
    -windowtitle "AudioStreaming - Documentação" \
    -doctitle "AudioStreaming - API Documentation" \
    -quiet

if [ $? -eq 0 ]; then
    echo
    echo "✔ Javadoc gerado com sucesso em: docs/javadoc"
    echo "  Abra docs/javadoc/index.html para visualizar."
else
    echo
    echo "❌ Erro ao gerar Javadoc."
fi
