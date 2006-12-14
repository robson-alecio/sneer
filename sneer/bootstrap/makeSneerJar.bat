rmdir ..\bin\sneerjartemp /s /q
mkdir ..\bin\sneerjartemp
javac -sourcepath .\src;..\src;..\..\wheel\src -d ..\bin\sneerjartemp .\src\sneer\boot\Boot.java
