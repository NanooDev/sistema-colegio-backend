#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

modules=(
  "$ROOT_DIR/base-biblioteca-salas/biblioteca.salas.duoc"
  "$ROOT_DIR/servicio-estudiantes"
  "$ROOT_DIR/servicio-profesores"
  "$ROOT_DIR/servicio-asignaturas"
  "$ROOT_DIR/servicio-asistencias"
  "$ROOT_DIR/servicio-biblioteca"
  "$ROOT_DIR/servicio-calificaciones"
  "$ROOT_DIR/servicio-finanzas"
  "$ROOT_DIR/servicio-matriculas"
  "$ROOT_DIR/servicio-notificaciones"
  "$ROOT_DIR/servicio-cursos"
)

for module in "${modules[@]}"; do
  name="$(basename "$module")"
  echo "=== $name ==="

  if [[ ! -x "$module/mvnw" ]]; then
    chmod +x "$module/mvnw"
  fi

  if (cd "$module" && ./mvnw -q test); then
    echo "OK $name"
  else
    echo "FAIL $name"
    exit 1
  fi

done

echo "Todas las suites pasaron correctamente."
