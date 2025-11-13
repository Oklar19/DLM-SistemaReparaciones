@echo off
chcp 65001 >nul
echo Construyendo sistema...

echo Compilando...
javac -cp "lib\sqlite-jdbc-3.51.0.0.jar" -d bin src\*.java

echo Creando JAR alternativo...
echo Creando carpeta temporal...
if exist temp rmdir /s /q temp
mkdir temp

echo Copiando clases...
xcopy bin\*.class temp\ /y >nul

echo Copiando MANIFEST...
copy MANIFEST.MF temp\ /y >nul

echo Creando run.bat...
echo @echo off > run.bat
echo java -cp "lib\sqlite-jdbc-3.51.0.0.jar;bin" SistemaReparaciones >> run.bat
echo pause >> run.bat

echo.
echo ¡LISTO! Ejecuta con 'run.bat'
echo El programa funciona desde las clases compiladas
pause