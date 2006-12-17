rmdir ..\bin\sneerjartemp /s /q
mkdir ..\bin\sneerjartemp
javac -encoding UTF-8 -sourcepath .\src;..\src;..\..\wheel\src -d ..\bin\sneerjartemp .\src\sneer\boot\Boot.java
jar cfe ..\bin\Sneer.jar sneer.boot.Boot -C ..\bin\sneerjartemp .
rmdir ..\bin\sneerjartemp /s /q

