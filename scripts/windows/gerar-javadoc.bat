@echo off
chcp 65001 > nul
echo ═══════════════════════════════════════════
echo        📚 Gerando Javadoc
echo ═══════════════════════════════════════════
echo.

cd /d "%~dp0..\.."

if exist "docs\javadoc" (
    echo Removendo Javadoc antigo...
    rmdir /s /q "docs\javadoc"
)

echo Gerando nova documentação...
javadoc -encoding UTF-8 -charset UTF-8 -docencoding UTF-8 ^
    -d docs/javadoc ^
    -subpackages main:model:service:menu:exception:interfaces ^
    -sourcepath src ^
    -windowtitle "AudioStreaming - Documentação" ^
    -doctitle "AudioStreaming - API Documentation" ^
    -quiet

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✔ Javadoc gerado com sucesso em: docs\javadoc
    echo   Abra docs\javadoc\index.html para visualizar.
) else (
    echo.
    echo ❌ Erro ao gerar Javadoc.
)

echo.
pause
