@echo off
setlocal
set "MAVEN_CMD=mvn"
if defined MAVEN_HOME if exist "%MAVEN_HOME%\bin\mvn.cmd" set "MAVEN_CMD=%MAVEN_HOME%\bin\mvn.cmd"
if defined MAVEN_HOME if exist "%MAVEN_HOME%\bin\mvn" set "MAVEN_CMD=%MAVEN_HOME%\bin\mvn"
if /I not "%~f0"=="%~dp0mvn.cmd" if exist "%~dp0mvn.cmd" set "MAVEN_CMD=%~dp0mvn.cmd"
if /I not "%~f0"=="%~dp0mvn" if exist "%~dp0mvn" set "MAVEN_CMD=%~dp0mvn"
"%MAVEN_CMD%" %*
