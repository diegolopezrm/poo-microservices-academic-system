#!/bin/bash

# ==========================================
# Script para detener todos los microservicios
# ==========================================

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$PROJECT_DIR"

echo "🛑 Deteniendo microservicios..."
echo ""

stop_service() {
    local name=$1
    local pid_file="$PROJECT_DIR/logs/$name.pid"
    
    if [ -f "$pid_file" ]; then
        local pid=$(cat "$pid_file")
        if ps -p $pid > /dev/null 2>&1; then
            kill $pid 2>/dev/null
            echo "  ✅ $name detenido (PID: $pid)"
        else
            echo "  ⚠️  $name no estaba corriendo"
        fi
        rm -f "$pid_file"
    else
        echo "  ⚠️  No se encontró PID para $name"
    fi
}

stop_service "api-gateway"
stop_service "ms-matriculas"
stop_service "ms-cursos"
stop_service "ms-estudiantes"

# También matar procesos Java que estén en los puertos
echo ""
echo "🔍 Verificando puertos..."

kill_port() {
    local port=$1
    local pid=$(lsof -ti :$port 2>/dev/null)
    if [ -n "$pid" ]; then
        kill $pid 2>/dev/null
        echo "  ✅ Puerto $port liberado"
    fi
}

kill_port 8080
kill_port 8081
kill_port 8082
kill_port 8083

echo ""
echo "✅ Todos los servicios detenidos"
echo ""
