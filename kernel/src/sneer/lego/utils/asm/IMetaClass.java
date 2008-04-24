package sneer.lego.utils.asm;

import java.io.File;
import java.io.IOException;

public interface IMetaClass {

    String getName();

    /* class name derived from classFile and root directory, not from bytecodes */
    String futureClassName();
    
    String getPackageName();

    File classFile();

    byte[] bytes() throws IOException;

    boolean isInterface();
}