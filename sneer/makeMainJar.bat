rmdir .\bin\mainjartemp /s /q
mkdir .\bin\mainjartemp
javac -encoding UTF-8 -sourcepath .\src;.\bootstrap\src;..\wheel\src -d .\bin\mainjartemp .\src\sneer\Main.java
jar cf .\bin\mainXXXXXX.jar -C .\bin\mainjartemp .
rmdir .\bin\mainjartemp /s /q

