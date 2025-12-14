@echo off
REM ============================================
REM Compilar AudioStreaming (Windows)
REM ============================================

cd /d "%~dp0..\.."
echo Compilando AudioStreaming...
javac -encoding UTF-8 -d bin -sourcepath src src/main/Projeto.java

if %ERRORLEVEL% EQU 0 (
    echo Compilacao concluida com sucesso!
) else (
    echo Erro na compilacao.
)
pause
