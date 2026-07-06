@echo off
setlocal
set "MAVEN_CMD=mvn"
if exist "%MAVEN_HOME%\bin\mvn.cmd" set "MAVEN_CMD=%MAVEN_HOME%\bin\mvn.cmd"
if exist "%MAVEN_HOME%\bin\mvn" set "MAVEN_CMD=%MAVEN_HOME%\bin\mvn"
if exist "%~dp0mvn.cmd" set "MAVEN_CMD=%~dp0mvn.cmd"
if exist "%~dp0mvn" set "MAVEN_CMD=%~dp0mvn"
"%MAVEN_CMD%" %*
