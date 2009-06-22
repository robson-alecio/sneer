package sneer.installer;

import java.io.File;

import sneer.main.SneerDirectories;

public class Directories extends SneerDirectories {

	static File SNEER_HOME() { return SNEER_HOME; }
	static File DATA() { return DATA; }
	static File LOG_FILE() { return LOG_FILE; }
	static File CODE() { return CODE; }
	static File OWN_CODE() { return OWN_CODE; }
	static File OWN_BIN() { return OWN_BIN; }
	static File PLATFORM_CODE() { return PLATFORM_CODE; }
	static File PLATFORM_BIN() { return PLATFORM_BIN; }
	
}
