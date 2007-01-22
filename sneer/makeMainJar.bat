rmdir .\bin\mainjartemp /s /q
mkdir .\bin\mainjartemp
javac -target 1.6 -source 1.6 -encoding UTF-8 -sourcepath .\src;.\bootstrap\src;..\wheel\src -d .\bin\mainjartemp .\src\sneer\Sneer.java
jar cf .\bin\mainXXXXXX.jar -C .\bin\mainjartemp .
rmdir .\bin\mainjartemp /s /q

