#!/bin/sh

#export JAVA_HOME=/usr/local/java/j2sdk1.4.1
export JAVA_HOME=/opt/IBMJava2-13
export WTK_HOME=/usr/local/java/WTK104

echo Creating directories
echo \(This stage may produce already exist errors. Ignore them.\)

mkdir ../tmpclasses
mkdir ../classes

echo Compiling source files

$JAVA_HOME/bin/javac -bootclasspath $WTK_HOME/lib/midpapi.zip -d ../tmpclasses -classpath ../tmpclasses ../reversi/*.java ../minimax/*.java ../util/*.java

echo Preverifying class files

$WTK_HOME/bin/preverify -classpath $WTK_HOME/lib/midpapi.zip:../tmpclasses -d ../classes ../tmpclasses

echo Jaring preverified class files
jar cmf MANIFEST.MF j2me_reversi.jar -C ../classes .

echo Jaring resource files
jar umf MANIFEST.MF j2me_reversi.jar -C ../res .

echo Don\'t forget to update the JAR file size in the JAD file

