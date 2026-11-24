@echo off
setlocal
chcp 65001 >nul

cd /d "%~dp0"

echo ===================================================
echo   Greenhouse - Zbirka proektu / Build
echo ===================================================

if exist "OOP-lab2\src" (
    set "SRC_DIR=OOP-lab2"
) else (
    set "SRC_DIR=."
)

echo [1/3] Perevirka Java...
where java >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java not found! Please install JDK 17+.
    pause
    exit /b 1
)

where mvn >nul 2>&1
if errorlevel 1 goto :no_maven

echo [2/3] Maven found. Building with Maven...
pushd "%SRC_DIR%"
call mvn clean package
popd
goto :post_build

:no_maven
echo [2/3] Maven not found. Building with javac and jar...
if not exist "%SRC_DIR%\target\classes" mkdir "%SRC_DIR%\target\classes"

powershell -NoProfile -Command "$files = Get-ChildItem -Path '%SRC_DIR%\src\main\java' -Recurse -Filter *.java | ForEach-Object { $_.FullName }; [System.IO.File]::WriteAllLines([System.IO.Path]::Combine($env:TEMP, 'greenhouse_src.txt'), $files)"

javac -encoding UTF-8 -d "%SRC_DIR%\target\classes" @"%TEMP%\greenhouse_src.txt"
if errorlevel 1 (
    echo [ERROR] Compilation failed!
    if exist "%TEMP%\greenhouse_src.txt" del "%TEMP%\greenhouse_src.txt"
    pause
    exit /b 1
)
if exist "%TEMP%\greenhouse_src.txt" del "%TEMP%\greenhouse_src.txt"

if exist "%SRC_DIR%\src\main\resources" (
    xcopy /s /y /q "%SRC_DIR%\src\main\resources\*" "%SRC_DIR%\target\classes\" >nul
)

if not exist "%SRC_DIR%\target" mkdir "%SRC_DIR%\target"
jar cfe "%SRC_DIR%\target\greenhouse-1.0.0.jar" ua.lab2.greenhouse.Main -C "%SRC_DIR%\target\classes" .
if errorlevel 1 (
    echo [ERROR] Failed to package JAR!
    pause
    exit /b 1
)

:post_build
echo [3/3] Creating build folder for direct execution...
if not exist "build" mkdir "build"
if not exist "build\data" mkdir "build\data"

if exist "%SRC_DIR%\target\greenhouse-1.0.0.jar" (
    copy /y "%SRC_DIR%\target\greenhouse-1.0.0.jar" "build\greenhouse.jar" >nul
)

xcopy /s /y /q "%SRC_DIR%\data\*" "build\data\" >nul

:: Create runner script inside build directory
echo @echo off > "build\run.bat"
echo chcp 65001 ^>nul >> "build\run.bat"
echo cd /d "%%~dp0" >> "build\run.bat"
echo echo =================================================== >> "build\run.bat"
echo echo   Greenhouse - Direct run from file >> "build\run.bat"
echo echo =================================================== >> "build\run.bat"
echo java -jar greenhouse.jar %%* >> "build\run.bat"
echo echo. >> "build\run.bat"
echo pause >> "build\run.bat"

if exist "OOP-lab2\pom.xml" (
    if not exist "OOP-lab2\build" mkdir "OOP-lab2\build"
    copy /y "build\greenhouse.jar" "OOP-lab2\build\" >nul
    if not exist "OOP-lab2\build\data" mkdir "OOP-lab2\build\data"
    xcopy /s /y /q "build\data\*" "OOP-lab2\build\data\" >nul
    copy /y "build\run.bat" "OOP-lab2\build\" >nul
    copy /y "build.bat" "OOP-lab2\build.bat" >nul
    copy /y "run.bat" "OOP-lab2\run.bat" >nul
)

echo.
echo ===================================================
echo   Build finished successfully!
echo   Run directly using:
echo     - run.bat
echo     - build\run.bat
echo ===================================================
echo.
pause
