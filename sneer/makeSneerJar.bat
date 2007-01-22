rmdir .\bin\sneerjartemp /s /q
mkdir .\bin\sneerjartemp

javac -target 1.1 -source 1.2 -encoding UTF-8 -sourcepath .\src              -d .\bin\sneerjartemp .\src\Boot.java
javac -target 1.6 -source 1.6 -encoding UTF-8 -sourcepath .\src;..\wheel\src -d .\bin\sneerjartemp .\src\sneer\strap\Strap.java
jar cfe .\bin\Sneer.jar Boot -C .\bin\sneerjartemp .

rmdir .\bin\sneerjartemp /s /q

java -jar .\bin\Sneer.jar
