@echo off
REM ============================================
REM Gerar JAR Executavel AudioStreaming (Windows)
REM ============================================

cd /d "%~dp0..\.."

echo Compilando...
javac -encoding UTF-8 -d bin -sourcepath src src/main/Projeto.java

if %ERRORLEVEL% NEQ 0 (
    echo Erro na compilacao.
    pause
    exit /b 1
)

echo Criando JAR...
cd bin
jar cfe ..\AudioStreaming.jar main.Projeto .
cd ..

if %ERRORLEVEL% EQU 0 (
    echo JAR criado com sucesso: AudioStreaming.jar
) else (
    echo Erro ao criar JAR.
)
pause
