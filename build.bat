cls
@echo off
javac -d "./GeneratedClassFiles" @JavaFiles

if %errorlevel% neq 0 (
	echo There was an error; exiting now.	
) else (
	echo Compiled correctly!  Running Game...
	java -classpath "./GeneratedClassFiles" Game	
)

