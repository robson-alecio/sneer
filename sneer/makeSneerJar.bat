rmdir .\bin\sneerjartemp /s /q
mkdir .\bin\sneerjartemp

md .\bin\sneerjartemp\sneer\
md .\bin\sneerjartemp\sneer\lib
md .\bin\sneerjartemp\sneer\lib\xstream
copy .\lib\*.* .\bin\sneerjartemp\sneer\lib
copy .\lib\xstream\*.* .\bin\sneerjartemp\sneer\lib\xstream

md .\bin\sneerjartemp\sneer\kernel
md .\bin\sneerjartemp\sneer\kernel\gui
md .\bin\sneerjartemp\sneer\kernel\gui\contacts
md .\bin\sneerjartemp\sneer\kernel\gui\contacts\images
copy .\src\sneer\kernel\gui\contacts\images\*.gif .\bin\sneerjartemp\sneer\kernel\gui\contacts\images

javac -target 1.1 -source 1.2 -encoding UTF-8 -sourcepath .\src -d .\bin\sneerjartemp .\src\sneer\Boot.java
javac -target 1.6 -source 1.6 -encoding UTF-8 -sourcepath .\src -d .\bin\sneerjartemp .\src\sneer\SneerJockey.java
javac -target 1.6 -source 1.6 -encoding UTF-8 -sourcepath .\src -d .\bin\sneerjartemp -cp .\bin\sneerjartemp\sneer\lib\prevayler-2.3.jar;.\bin\sneerjartemp\sneer\lib\xstream\xstream-1.2.2.jar .\src\sneer\Sneer.java

jar cfem .\bin\Sneer.jar sneer.Boot makeSneerJar.manifest -C .\bin\sneerjartemp .
rmdir .\bin\sneerjartemp /s /q

rmdir c:\sneerteste /s /q
mkdir c:\sneerteste
java -Dsneer.user_home_override=c:\lixo -jar .\bin\Sneer.jar
