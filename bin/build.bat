set JAVA_HOME=e:\Programs\jdk1.3.1_01
set WTK_HOME=e:\Programs\WTK104

rem echo Creating directories
rem echo \(This stage may produce already exist errors. Ignore them.\)

mkdir ..\tmpclasses
mkdir ..\classes

rem echo Compiling source files

%JAVA_HOME%\bin\javac -bootclasspath %WTK_HOME%/lib/midpapi.zip -d ..\tmpclasses -classpath ..\tmpclasses ..\reversi\*.java ..\minimax\*.java ..\util\*.java

rem echo Preverifying class files

%WTK_HOME%\bin\preverify -classpath %WTK_HOME%\lib\midpapi.zip;..\tmpclasses -d ..\classes ..\tmpclasses

rem echo Jaring preverified class files
%JAVA_HOME%\bin\jar cmf MANIFEST.MF j2me_reversi.jar -C ..\classes .

echo Jaring resource files
%JAVA_HOME%\bin\jar umf MANIFEST.MF j2me_reversi.jar -C ..\res .

rem echo Don\'t forget to update the JAR file size in the JAD file

