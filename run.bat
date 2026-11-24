@echo off
setlocal
chcp 65001 >nul

cd /d "%~dp0"

echo ===================================================
echo   Greenhouse - Launching Application
echo ===================================================

if exist "%~dp0build\greenhouse.jar" goto :found_local_build
if exist "%~dp0OOP-lab2\build\greenhouse.jar" goto :found_sub_build
if exist "%~dp0greenhouse.jar" goto :found_here

echo [INFO] Build not found. Running build.bat...
if exist "%~dp0build.bat" call "%~dp0build.bat"
if not exist "%~dp0build.bat" if exist "%~dp0OOP-lab2\build.bat" call "%~dp0OOP-lab2\build.bat"

if exist "%~dp0build\greenhouse.jar" goto :found_local_build
if exist "%~dp0OOP-lab2\build\greenhouse.jar" goto :found_sub_build

echo [ERROR] Could not find or build greenhouse.jar!
pause
exit /b 1

:found_local_build
cd /d "%~dp0build"
goto :run_jar

:found_sub_build
cd /d "%~dp0OOP-lab2\build"
goto :run_jar

:found_here
cd /d "%~dp0"
goto :run_jar

:run_jar
java -jar greenhouse.jar %*
echo.
echo ---------------------------------------------------
echo Program finished. Press any key to close...
pause
