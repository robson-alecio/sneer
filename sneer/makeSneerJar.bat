rmdir .\bin\sneerjartemp /s /q
mkdir .\bin\sneerjartemp

xcopy .\lib\prevayler-2.3\*.* .\bin\sneerjartemp /S
xcopy .\lib\xstream\xstream-1.2.2\*.* .\bin\sneerjartemp /S
xcopy .\lib\xstream\xpp3_min-1.1.3.4.O\*.* .\bin\sneerjartemp /S

javac -target 1.1 -source 1.2 -encoding UTF-8 -sourcepath .\src -d .\bin\sneerjartemp .\src\sneer\Boot.java
javac -target 1.6 -source 1.6 -encoding UTF-8 -sourcepath .\src -d .\bin\sneerjartemp .\src\sneer\SneerJockey.java
javac -target 1.6 -source 1.6 -encoding UTF-8 -sourcepath .\src -d .\bin\sneerjartemp -cp .\bin\sneerjartemp .\src\sneer\Sneer.java

copy .\src\Translation*.* .\bin\sneerjartemp

md .\bin\sneerjartemp\sneer\
md .\bin\sneerjartemp\sneer\kernel
md .\bin\sneerjartemp\sneer\kernel\gui
md .\bin\sneerjartemp\sneer\kernel\gui\traymenu
copy .\src\sneer\kernel\gui\traymenu\*.png .\bin\sneerjartemp\sneer\kernel\gui\traymenu

md .\bin\sneerjartemp\sneer\kernel\gui\contacts
md .\bin\sneerjartemp\sneer\kernel\gui\contacts\images
copy .\src\sneer\kernel\gui\contacts\images\*.gif .\bin\sneerjartemp\sneer\kernel\gui\contacts\images

jar cfe .\bin\Sneer.jar sneer.Boot -C .\bin\sneerjartemp .
rmdir .\bin\sneerjartemp /s /q

rmdir c:\sneerteste /s /q
mkdir c:\sneerteste
java -Dsneer.user_home_override=c:\lixo -jar .\bin\Sneer.jar
