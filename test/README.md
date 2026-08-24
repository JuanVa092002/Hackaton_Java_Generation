# QA ClinicaApp

Pruebas del hackaton: modelo (`equals`, setters, estado inicial), reglas de `ClinicaService` (duplicados, agenda, consultas, cancelar) y persistencia CSV.

## Cómo correrlas

Desde la **raíz del proyecto**, en Git Bash o terminal:

```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name "*.java")
javac -encoding UTF-8 -cp out -d out test/qa/QaHackaton.java
java -cp out qa.QaHackaton
```

En PowerShell:

```powershell
New-Item -ItemType Directory -Force -Path out | Out-Null
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java | ForEach-Object FullName)
javac -encoding UTF-8 -cp out -d out test\qa\QaHackaton.java
java -cp out qa.QaHackaton
```

Salida esperada: todas las líneas `PASS` y `Resultado: N PASS, 0 FAIL`.

La prueba de persistencia escribe en `datos/` (carpeta del enunciado). No hace falta subir esos CSV.
