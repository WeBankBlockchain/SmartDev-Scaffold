@echo off
setlocal

set SOL_DIR=%cd%\contracts
if not exist %SOL_DIR% (
    echo %SOL_DIR% directory not exists. Please copy solidity files here
    exit /B 0
)
set TOOLS_DIR=%cd%
for /f "delims=" %%i in ('type "config.ini"^| find /i "="') do set %%i
echo GROUP=%GROUP%
echo ARTIFACT=%ARTIFACT%
echo SOL_DIR=%SOL_DIR%
echo TOOLS_DIR=%TOOLS_DIR%
echo SELECTOR=%SELECTOR%
echo COMPILER=%COMPILER%

if exist %ARTIFACT% (
    echo Artifact "%ARTIFACT%" ALREADY exists. Please remove it if you want to override
    exit /B 0
)

echo start compiling scaffold...
cd ..
gradle clean shadowJar -PsolcVersion=%COMPILER% | more
echo end compiling scaffold...

echo start generating %ARTIFACT%...

echo %TOOLS_DIR%
if [%SELECTOR%]==[] (
    java -jar dist/WeBankBlockchain-SmartDev-Scaffold.jar -g %GROUP% -a %ARTIFACT% -s %SOL_DIR% -o %TOOLS_DIR%
) else (
    java -jar dist/WeBankBlockchain-Smartdev-Scaffold.jar -g %GROUP% -a %ARTIFACT% -s %SOL_DIR% -o %TOOLS_DIR% -n %SELECTOR%
)


exit /B 0


