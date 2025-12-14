@echo off
REM ============================================
REM Compilar Testes JUnit (Windows)
REM ============================================

cd /d "%~dp0..\.."

REM Cria arquivo temporario com lista de arquivos de teste
echo Compilando codigo e testes...

REM Lista todos os arquivos .java de teste recursivamente
dir /s /b src\test\*.java > test-sources-temp.txt 2>nul

REM Verifica se encontrou arquivos de teste
if not exist test-sources-temp.txt (
    echo Nenhum arquivo de teste encontrado em src\test
    pause
    exit /b 1
)

REM Compila o codigo principal e os testes usando o arquivo de lista
javac -encoding UTF-8 -d bin -cp "lib\*" -sourcepath src src\main\Projeto.java @test-sources-temp.txt

set COMPILE_RESULT=%ERRORLEVEL%

REM Remove arquivo temporario
del test-sources-temp.txt 2>nul

if %COMPILE_RESULT% EQU 0 (
    echo Compilacao concluida com sucesso!
) else (
    echo Erro na compilacao.
)
pause
