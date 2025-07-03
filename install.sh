#!/bin/bash

set -e

REPO="otavioabreu27/tree"
BINARY_NAME="tree"
INSTALL_PATH="/usr/local/bin/$BINARY_NAME"

echo "🔍 Buscando a última versão do $BINARY_NAME..."

LATEST_RELEASE=$(curl -s "https://api.github.com/repos/$REPO/releases/latest")
ASSET_URL=$(echo "$LATEST_RELEASE" | grep "browser_download_url" | grep "$BINARY_NAME" | cut -d '"' -f 4)

if [ -z "$ASSET_URL" ]; then
  echo "❌ Não foi possível encontrar o binário na última release."
  exit 1
fi

echo "⬇️ Baixando binário de: $ASSET_URL"
curl -L "$ASSET_URL" -o "/tmp/$BINARY_NAME"

echo "🚚 Movendo binário para $INSTALL_PATH"
sudo mv "/tmp/$BINARY_NAME" "$INSTALL_PATH"

echo "🔐 Concedendo permissão de execução"
sudo chmod +x "$INSTALL_PATH"

echo "✅ Instalação concluída! Agora você pode rodar o comando:"
echo ""
echo "    $BINARY_NAME"
