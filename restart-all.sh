#!/bin/bash

# ==========================================
# Script para reiniciar todos los microservicios
# ==========================================

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$PROJECT_DIR"

echo "🔄 Reiniciando microservicios..."
echo ""

./stop-all.sh
sleep 2
./start-all.sh
