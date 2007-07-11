LEGAL STUFF
===============

See license.txt in this same repository or distribution for licensing info.   

The copyright of all files in this repository or distribution, except the files contained in the lib directory and subdirectories, is held by Klaus Wuestefeld.

BY COMMITTING ANY FILES, INCLUDING, BUT NOT LIMITED TO, SOURCE CODE, IMAGES, DOCUMENTS, DESIGNS AND DRAWINGS, TO THE SNEER PROJECT REPOSITORY YOU ARE TRANFERRING ALL RIGHTS YOU HAVE ON THE CONTENTS OF THOSE FILES INCLUDING, BUT NOT LIMITED TO, COPYRIGHTS, PATENTS AND ALL OTHER INTELLECTUAL PROPERTY RIGHTS, IMMEDIATELY, ETERNALLY AND IRREVOCABLY TO KLAUS WUESTEFELD.
YOU ARE NOT ALLOWED TO COMMIT TO THE SNEER PROJECT REPOSITORY ANY FILE IF YOU DO NOT OWN ALL INTELLECTUAL PROPERTY RIGHTS TO THE CONTENTS OF THAT FILE, INCLUDING, BUT NOT LIMITED TO, ALL MENTIONED ABOVE. IF YOU WANT THE SNEER PROJECT REPOSITORY TO INCLUDE A CLASS LIBRARY OR ANY OTHER MATERIAL OWNED BY A THIRD PARTY, YOU MUST ASK KLAUS WUESTEFELD TO COMMIT IT.


GETTING SNEER SOURCE CODE
=============================

http://sovereigncomputing.net/svn/sneer/ - is the Subversion (SVN) repository root. https works too.


BUILDING SNEER
==================

JRE6 - Use the latest stable version of it. Before reporting bugs or problems, please make sure you are not using another JRE.

makeSneerJar.bat - This will produce the runnable Sneer.jar file. If you want to contribute an ANT file to produce EXACTLY the same jar file, so that it can run on Linux too, that would be great.

Eclipse 3.3 or newer - You can use other IDEs but it is strongly recommended that you use Eclipse because Sneer is a self-contained Eclipse project and will compile out of the box with zero errors and zero warnings.

Anyway:
Sourcepath: src; src_old; spikes
Classpath: all jars inside the lib directory
Main class: src/sneer.tests.MainSkippingBoot


"WHAT CAN I DO TO HELP?"
============================

That's the spirit.  :)

1) Use Sneer.

2) Show Sneer to other people and help them use it.

3) Translate the Sneer language file to new languages.

4) Report problems and bugs.

5) Discuss your wishes and ideas for Sneer.

6) Develop your own sovereign application to run on Sneer.

7) Code for Sneer.


CODING FOR SNEER
====================

If you have done all the rest and are ready to code for Sneer, start by searching comments in the code with the words Fix, Refactor, Implement and Optimize (or simply open the Eclipse Tasks View). That, and only that, is how we track feature and issue tasks.

Find the EASIEST, MOST TRIVIAL task to do and submit a patch.

Repeat until you get the hang of it and we gain confidence in you to give you commiter rights.

Writing JUnit tests to improve test coverage is also a very popular move.  


GETTING IN TOUCH
====================

sneercoders@googlegroups.com

See you there, Klaus.  :)
