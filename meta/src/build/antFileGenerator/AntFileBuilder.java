package build.antFileGenerator;

public interface AntFileBuilder {

	void addClasspathEntry(String lib);
	void addCompileEntry(String src);
	
	void build();

}
