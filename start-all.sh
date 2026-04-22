#!/bin/bash

# ==========================================
# Script para iniciar todos los microservicios
# ==========================================

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$PROJECT_DIR"

echo "🔨 Compilando proyecto..."
mvn clean package -DskipTests -q

if [ $? -ne 0 ]; then
    echo "❌ Error en la compilación"
    exit 1
fi

echo ""
echo "🚀 Iniciando microservicios..."
echo ""

# Crear directorio de logs
mkdir -p logs

# Función para iniciar un servicio
start_service() {
    local name=$1
    local dir=$2
    local port=$3
    
    echo "  ▶ Iniciando $name en puerto $port..."
    cd "$PROJECT_DIR/$dir"
    nohup java -jar target/*.jar > "$PROJECT_DIR/logs/$name.log" 2>&1 &
    echo $! > "$PROJECT_DIR/logs/$name.pid"
    cd "$PROJECT_DIR"
}

# Iniciar servicios en orden
start_service "ms-estudiantes" "ms-estudiantes" 8081
sleep 2

start_service "ms-cursos" "ms-cursos" 8082
sleep 2

start_service "ms-matriculas" "ms-matriculas" 8083
sleep 2

start_service "api-gateway" "api-gateway" 8080

echo ""
echo "⏳ Esperando que los servicios inicien (15s)..."
sleep 15

echo ""
echo "✅ Verificando servicios..."
echo ""

check_health() {
    local name=$1
    local port=$2
    local status=$(curl -s -o /dev/null -w "%{http_code}" "http://localhost:$port/actuator/health" 2>/dev/null || echo "000")
    
    if [ "$status" = "200" ]; then
        echo "  ✅ $name (puerto $port): UP"
    else
        echo "  ❌ $name (puerto $port): DOWN"
    fi
}

check_health "MS-Estudiantes" 8081
check_health "MS-Cursos" 8082
check_health "MS-Matriculas" 8083
check_health "API-Gateway" 8080

echo ""
echo "🎉 Sistema iniciado!"
echo ""
echo "📍 URLs:"
echo "   • API Gateway:     http://localhost:8080"
echo "   • MS-Estudiantes:  http://localhost:8081"
echo "   • MS-Cursos:       http://localhost:8082"
echo "   • MS-Matriculas:   http://localhost:8083"
echo ""
echo "📋 Comandos:"
echo "   • Ver logs:    tail -f logs/*.log"
echo "   • Detener:     ./stop-all.sh"
echo ""
