rmdir .\bin\sneerjartemp /s /q
mkdir .\bin\sneerjartemp

javac -target 1.1 -source 1.2 -encoding UTF-8 -sourcepath .\src -d .\bin\sneerjartemp .\src\sneer\Boot.java
javac -target 1.6 -source 1.6 -encoding UTF-8 -sourcepath .\src -d .\bin\sneerjartemp .\src\sneer\SneerJockey.java
javac -target 1.6 -source 1.6 -encoding UTF-8 -sourcepath .\src -d .\bin\sneerjartemp .\src\sneer\Sneer.java

md .\bin\sneerjartemp\sneer\gui
md .\bin\sneerjartemp\sneer\gui\traymenu
copy .\src\sneer\gui\traymenu\*.png .\bin\sneerjartemp\sneer\gui\traymenu

jar cfe .\bin\Sneer.jar sneer.Boot -C .\bin\sneerjartemp .
rmdir .\bin\sneerjartemp /s /q

java -Dsneer.testmode=true -jar .\bin\Sneer.jar
