@echo off
IF %1.==. GOTO err
GOTO st


:err
	ECHO USAGE - run filePath
	GOTO end1
:st
	java -classpath ".\out\production\JCells" main.jcells.main.jcells %1
	
:end1