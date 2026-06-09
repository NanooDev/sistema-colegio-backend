#!/usr/bin/env bash
set -euo pipefail

# Starts MySQL (docker-compose) and both services in background, writing logs to each service's logs/ folder.
# Usage: ./scripts/start_services.sh

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"

echo "Starting MySQL via docker compose..."
cd "$ROOT_DIR"
docker compose -f docker-compose.local.yml up -d

echo "Waiting for MySQL to be ready (this may take a few seconds)..."
until docker exec mysql-sch mysqladmin ping -uroot -proot --silent; do
  sleep 1
done
echo "MySQL is up."

mkdir -p "$ROOT_DIR/servicio-estudiantes/logs" "$ROOT_DIR/servicio-profesores/logs"

echo "Starting servicio-estudiantes in background..."
cd "$ROOT_DIR/servicio-estudiantes"
chmod +x mvnw
nohup ./mvnw -DskipTests spring-boot:run > logs/out-estudiantes.log 2>&1 &
sleep 1

echo "Starting servicio-profesores in background..."
cd "$ROOT_DIR/servicio-profesores"
chmod +x mvnw
nohup ./mvnw -DskipTests spring-boot:run > logs/out-profesores.log 2>&1 &

echo "All start commands issued. Follow logs with:"
echo "  tail -f $ROOT_DIR/servicio-estudiantes/logs/out-estudiantes.log $ROOT_DIR/servicio-profesores/logs/out-profesores.log"
