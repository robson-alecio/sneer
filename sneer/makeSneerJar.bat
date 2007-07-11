rmdir .\bin\sneerjartemp /s /q
mkdir .\bin\sneerjartemp

cd .\bin\sneerjartemp

copy ..\..\lib\prevayler-2.3.jar
copy ..\..\lib\xstream\xstream-1.2.2.jar
copy ..\..\lib\xstream\xpp3_min-1.1.3.4.O.jar

jar -xf prevayler-2.3.jar
jar -xf xstream-1.2.2.jar
jar -xf xpp3_min-1.1.3.4.O.jar

javac -target 1.1 -source 1.2 -encoding UTF-8 -sourcepath ..\..\src -d .       ..\..\src\sneer\Boot.java
javac -target 1.6 -source 1.6 -encoding UTF-8 -sourcepath ..\..\src -d .       ..\..\src\sneer\SneerJockey.java
javac -target 1.6 -source 1.6 -encoding UTF-8 -sourcepath ..\..\src -d . -cp . ..\..\src\sneer\Sneer.java

md translations
copy ..\..\src\translations\*.* .\translations

md .\sneer\kernel\gui\traymenu
copy ..\..\src\sneer\kernel\gui\traymenu\*.png .\sneer\kernel\gui\traymenu

md .\sneer\kernel\gui\contacts\images
copy ..\..\src\sneer\kernel\gui\contacts\images\*.gif .\sneer\kernel\gui\contacts\images

jar cfe ..\..\bin\Sneer.jar sneer.Boot .

cd ..\..
rmdir .\bin\sneerjartemp /s /q

rmdir c:\sneerteste /s /q
mkdir c:\sneerteste
java -Dsneer.user_home_override=c:\sneerteste -jar .\bin\Sneer.jar
