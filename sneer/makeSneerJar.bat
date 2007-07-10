rmdir .\bin\sneerjartemp /s /q
mkdir .\bin\sneerjartemp

javac -target 1.1 -source 1.2 -encoding UTF-8 -sourcepath .\src -d .\bin\sneerjartemp .\src\sneer\Boot.java
javac -target 1.6 -source 1.6 -encoding UTF-8 -sourcepath .\src -d .\bin\sneerjartemp .\src\sneer\SneerJockey.java
javac -target 1.6 -source 1.6 -encoding UTF-8 -sourcepath .\src -d .\bin\sneerjartemp -cp .\lib\prevayler-2.3.jar;.\lib\xstream\xstream-1.2.2.jar .\src\sneer\Sneer.java

md .\bin\sneerjartemp\lib
copy .\lib\prevayler-2.3.jar .\bin\sneerjartemp\lib
copy .\lib\xstream\xstream-1.2.2.jar .\bin\sneerjartemp\lib
copy .\lib\xstream\xpp3_min-1.1.3.4.O.jar .\bin\sneerjartemp\lib

md .\bin\sneerjartemp\translations
copy .\src\translations\*.* .\bin\sneerjartemp\translations

md .\bin\sneerjartemp\sneer\kernel\gui\traymenu
copy .\src\sneer\kernel\gui\traymenu\*.png .\bin\sneerjartemp\sneer\kernel\gui\traymenu

md .\bin\sneerjartemp\sneer\kernel\gui\contacts\images
copy .\src\sneer\kernel\gui\contacts\images\*.gif .\bin\sneerjartemp\sneer\kernel\gui\contacts\images

jar cfe .\bin\Sneer.jar sneer.Boot -C .\bin\sneerjartemp .
rmdir .\bin\sneerjartemp /s /q

rmdir c:\sneerteste /s /q
mkdir c:\sneerteste
java -Dsneer.user_home_override=c:\sneerteste -jar .\bin\Sneer.jar
