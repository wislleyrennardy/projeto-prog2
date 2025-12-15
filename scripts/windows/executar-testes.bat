@echo off
REM ============================================
REM Executar Testes JUnit (Windows)
REM ============================================

cd /d "%~dp0..\.."
chcp 65001 > nul
echo Executando todos os testes...
java -Dfile.encoding=UTF-8 -jar lib\junit-platform-console-standalone-1.10.2.jar execute --class-path bin --scan-classpath
pause
