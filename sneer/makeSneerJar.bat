rmdir .\bin\sneerjartemp /s /q
mkdir .\bin\sneerjartemp

javac -target 1.1 -source 1.2 -encoding UTF-8 -sourcepath .\src -d .\bin\sneerjartemp .\src\sneer\boot\Boot.java
javac -target 1.6 -source 1.6 -encoding UTF-8 -sourcepath .\src -d .\bin\sneerjartemp .\src\sneer\boot\KernelJockey.java
javac -target 1.6 -source 1.6 -encoding UTF-8 -sourcepath .\src -d .\bin\sneerjartemp .\src\sneer\kernel\Kernel.java
copy .\src\sneer\kernel\*.png .\bin\sneerjartemp\sneer\kernel

jar cfe .\bin\Sneer.jar sneer.boot.Boot -C .\bin\sneerjartemp .

cd .\bin
rmdir .sneer /s /q
java -Dsneer.testmode=true -jar Sneer.jar
cd ..
