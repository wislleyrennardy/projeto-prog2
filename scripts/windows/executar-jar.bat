@echo off
REM ============================================
REM Executar AudioStreaming via JAR (Windows)
REM Configura UTF-8 para exibir emojis corretamente
REM ============================================

cd /d "%~dp0..\.."
chcp 65001 > nul
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -jar AudioStreaming.jar
pause
