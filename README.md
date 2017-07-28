# jtReversi (a.k.a. J2ME_Reversi)

This is an obsolete project. Code moved from http://j2mereversi.sf.net 
( https://sourceforge.net/projects/j2mereversi/ )

What is it?
-----------

This is a simple board game. It is also known as othello. If you don't
know the rules, just browse the web, you can find hundreds of reversi
games. A short description is included in the game.

This version of the game runs on devices compliant with the Mobile
Information Device Profile (MIDP), for instance cellular phones.

Note: The program has been tested with several real MIDP devices.

Why?
----

I wanted to learn J2ME, so I implemented this small game back in 2002. 

Before that I've implemented reversi using Turbo Pascal, later I
converted it to C (it was an artificial intelligence project for my
University) and later I converted it to Java (J2ME).

How to compile?
---------------

The game is precompiled, so if you want only to play with the game,
you don't need to compile it. 

If you really want to compile, you'll need the followings obsolete tools:

- Java 2 JDK (tested with Sun's Java 2 JDK 1.4.1 for Linux)
- J2ME wireless toolkit (tested with version 1.0.4)
- Jakarta-Ant (at least version 1.5)
- antenna (version 0.9.5 or newer)

0. set JAVA_HOME environment variable
1. Change the path of wtk.home in .ant.properties file.
2. ant

How to compile with obfuscator?
-------------------------------

If you use obfuscator, the size of the jar file will be smaller. Currently
the build process (antenna) can use two obfuscators: ProGuard (tested with 
versions 1.4, 1.5, 1.6) and RetroGuard-1.1.

0. set JAVA_HOME environment variable
1. Copy (or symlink) proguard.jar or retroguard.jar into WTK's bin directory.
2. Change the obfuscate="false" to obfucate="true" in build.xml
3. ant

NOTE: RetroGuard-1.1 is not working for me with J2SDK 1.4.1 for Linux
NOTE2: ProGuard creates smaller jar file for this project.

How to run?
-----------

If you want to try the program, you'll need:

J2ME wireless toolkit (tested with version 1.0.4)

1. Go to the bin directory
2. Change the path of WTK_HOME.
3. ./run.sh (Windows users have to use run.bat instead of run.sh)

How to run with Ant?
--------------------

If you have ant installed you can run the program using ant, instead of
the run.sh (run.bat) scripts.

0. set JAVA_HOME environment variable
1. Change the path of wtk.home in .ant.properties file.
2. ant run 

With Ant you are also able to test the program with microemulator 
 https://sourceforge.net/projects/microemulator/ : 

0. set JAVA_HOME environment variable
1. Change the path of MICROEMULATOR_HOME in .ant.properties file.
2. ant microrun

Note: Please use at least version 0.3.1 of microemulator. Parts of the
program may not work even with the current CVS version.

Licensing
---------

GPL (See COPYING)

Authors
------

- [András Salamon](https://github.com/asalamon74)
- [Richárd Szabó](https://github.com/richardszabo)
