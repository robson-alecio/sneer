LEGAL STUFF
===============

See license.txt in this same repository or distribution for licensing info.   

The copyright of all files in this repository or distribution, except the files contained in the lib directories and subdirectories, is held by Klaus Wuestefeld.

BY COMMITTING ANY FILES, INCLUDING, BUT NOT LIMITED TO, SOURCE CODE, IMAGES, DOCUMENTS, DESIGNS AND DRAWINGS, TO THE SNEER PROJECT REPOSITORY YOU ARE TRANFERRING ALL RIGHTS YOU HAVE ON THE CONTENTS OF THOSE FILES INCLUDING, BUT NOT LIMITED TO, COPYRIGHTS, PATENTS AND ALL OTHER INTELLECTUAL PROPERTY RIGHTS, IMMEDIATELY, ETERNALLY AND IRREVOCABLY TO KLAUS WUESTEFELD.
YOU ARE NOT ALLOWED TO COMMIT TO THE SNEER PROJECT REPOSITORY ANY FILE IF YOU DO NOT OWN ALL INTELLECTUAL PROPERTY RIGHTS TO THE CONTENTS OF THAT FILE, INCLUDING, BUT NOT LIMITED TO, ALL MENTIONED ABOVE. IF YOU WANT THE SNEER PROJECT REPOSITORY TO INCLUDE A CLASS LIBRARY OR ANY OTHER MATERIAL OWNED BY A THIRD PARTY, YOU MUST ASK KLAUS WUESTEFELD TO COMMIT IT.


RUNNING SNEER SOURCE CODE
=============================

http://sovereigncomputing.net/svn/sneer/ - is the Subversion (SVN) repository root. https works too. Just check out trunk/sneer.

JDK6 - Use the latest stable version of it. Before reporting bugs or problems, please make sure you are not using another JDK. To build Sneer we use the java compiler, so it has to be JDK, not the JRE.

Eclipse 3.4 or newer - You can use other IDEs but it is strongly recommended that you use Eclipse because Sneer is a self-contained Eclipse project and will compile out of the box with zero errors and zero warnings.

build.xml - If you don't use Eclipse, this ANT build file will compile Sneer and run all tests.

Main Class: main.Sneer - Run it and follow usage instructions. You can also run main.SneerDummy for testing with two Sneer instances running.

home_override - You can set this Java system property to make Sneer run in a different directory, so you can have several different Sneer installations running at the same time. Example: java -Dhome_override=some/other/directory main.Sneer

JUnit Tests - They are found inside the *.tests packages. For example, the sneer.foo.tests package would contain the JUnit tests referring to the sneer.foo package.

Functional Tests - They are the Freedom* classes. They are JUnit tests too, but are so high-level they use adapters to run against the actual implementation. Delegating to adapters keeps the tests clear without implementation details. The actual concrete adapters are the SneerFreedom* classes.


"WHAT CAN I DO TO HELP?"
============================

That's the spirit.  :)

1) Use Sneer.

2) Show Sneer to other people and help them use it.

3) Report problems and bugs.

4) Discuss your wishes and ideas for Sneer.

5) Develop your own sovereign applications to run on Sneer.

6) Get well-known in the sovereign development community.

7) Come work with us on Sneer.


GETTING IN TOUCH
====================

http://groups.google.com/group/sneercoders


See you there, Klaus.  :)
