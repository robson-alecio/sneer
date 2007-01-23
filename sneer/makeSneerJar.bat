rmdir .\bin\sneerjartemp /s /q
mkdir .\bin\sneerjartemp
javac -target 1.1 -source 1.2 -encoding UTF-8 -sourcepath .\src              -d .\bin\sneerjartemp .\src\Boot.java
javac -target 1.6 -source 1.6 -encoding UTF-8 -sourcepath .\src;..\wheel\src -d .\bin\sneerjartemp .\src\sneer\strap\Strap.java
jar cfe .\bin\Sneer.jar Boot -C .\bin\sneerjartemp .

rmdir .\bin\mainjartemp /s /q
mkdir .\bin\mainjartemp
javac -target 1.6 -source 1.6 -encoding UTF-8 -sourcepath .\src;.\bootstrap\src;..\wheel\src -d .\bin\mainjartemp .\src\sneer\Sneer.java
jar cf .\bin\main000001.jar -C .\bin\mainjartemp .

rmdir .\bin\servertemp /s /q
mkdir .\bin\servertemp
javac -target 1.6 -source 1.6 -encoding UTF-8 -sourcepath .\src;..\wheel\src -d .\bin\servertemp .\src\sneer\server\Server.java
javac -target 1.6 -source 1.6 -encoding UTF-8 -sourcepath .\src;..\wheel\src -d .\bin\servertemp .\src\sneer\strap\VersionUpdateAgent.java
 java -Dsneer.testmode=true -cp .\bin\servertemp sneer.server.Server

cd bin
rmdir .sneer /s /q
java -Dsneer.testmode=true -jar Sneer.jar
cd ..
