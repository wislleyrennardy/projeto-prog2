@echo off
REM ============================================
REM Compilar Testes JUnit (Windows)
REM ============================================

cd /d "%~dp0..\.."
echo Compilando codigo e testes...
javac -encoding UTF-8 -d bin -cp "lib\*" -sourcepath src src/main/Projeto.java src/test/*.java

if %ERRORLEVEL% EQU 0 (
    echo Compilacao concluida com sucesso!
) else (
    echo Erro na compilacao.
)
pause
