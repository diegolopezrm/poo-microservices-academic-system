#!/bin/bash

# ==========================================
# Script para verificar estado de servicios
# ==========================================

echo "📊 Estado de los microservicios:"
echo ""

check_health() {
    local name=$1
    local port=$2
    
    local response=$(curl -s "http://localhost:$port/actuator/health" 2>/dev/null)
    local status=$(echo "$response" | grep -o '"status":"[^"]*"' | head -1 | cut -d'"' -f4)
    
    if [ "$status" = "UP" ]; then
        echo "  ✅ $name (puerto $port): UP"
    elif [ -z "$status" ]; then
        echo "  ❌ $name (puerto $port): NO DISPONIBLE"
    else
        echo "  ⚠️  $name (puerto $port): $status"
    fi
}

check_health "MS-Estudiantes" 8081
check_health "MS-Cursos" 8082
check_health "MS-Matriculas" 8083
check_health "API-Gateway" 8080

echo ""
