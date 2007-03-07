rmdir .\bin\sneerjartemp /s /q
mkdir .\bin\sneerjartemp
javac -target 1.1 -source 1.2 -encoding UTF-8 -sourcepath .\src -d .\bin\sneerjartemp .\src\sneerloader\SneerLoader.java
javac -target 1.6 -source 1.6 -encoding UTF-8 -sourcepath .\src -d .\bin\sneerjartemp .\src\sneer\Platform.java
jar cfe .\bin\Sneer.jar sneerloader.SneerLoader -C .\bin\sneerjartemp .

rmdir .\bin\mainjartemp /s /q
mkdir .\bin\mainjartemp
javac -target 1.6 -source 1.6 -encoding UTF-8 -sourcepath .\src;.\bootstrap\src;..\wheel\src -d .\bin\mainjartemp .\src\sneer\Sneer.java
copy .\bin\sneer\*.png .\bin\mainjartemp\sneer
rmdir .\bin\mainapps /s /q
mkdir .\bin\mainapps
jar cf .\bin\mainapps\main000001.jar -C .\bin\mainjartemp .

rmdir .\bin\servertemp /s /q
mkdir .\bin\servertemp
javac -target 1.6 -source 1.6 -encoding UTF-8 -sourcepath .\src;..\wheel\src -d .\bin\servertemp .\src\sneer\server\Server.java
javac -target 1.6 -source 1.6 -encoding UTF-8 -sourcepath .\src;..\wheel\src -d .\bin\servertemp .\src\sneer\strap\VersionUpdateAgent.java
cd bin
start javaw -Dsneer.testmode=true -cp .\servertemp sneer.server.Server

rmdir .sneer /s /q
java -Dsneer.testmode=true -jar Sneer.jar
cd ..
