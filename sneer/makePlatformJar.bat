rmdir .\bin\platformjartemp /s /q
mkdir .\bin\platformjartemp
javac -target 1.6 -source 1.6 -encoding UTF-8 -sourcepath .\src -d .\bin\platformjartemp .\src\sneer\Platform.java
jar cf .\bin\platformXXXXXX.jar -C .\bin\platformjartemp .
rmdir .\bin\platformjartemp /s /q

