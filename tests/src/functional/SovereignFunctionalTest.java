package functional;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;

import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.Inject;
import sneer.kernel.container.impl.classloader.ApiClassLoader;
import sneer.kernel.container.tests.BrickTestSupport;
import sneer.pulp.tmpdirectory.TmpDirectory;

public abstract class SovereignFunctionalTest extends BrickTestSupport {

	protected abstract SovereignCommunity createNewCommunity();

	protected SovereignCommunity _community;
	
	protected SovereignParty _a;
	protected SovereignParty _b;
	
	@Inject
	private TmpDirectory _tmpDirectory;

	
	@Before
	public void initNewCommunity() {
		_community = createNewCommunity();
		createAndConnectParties();
	}
	
	@After
	public void cleanUpDirectories() throws IOException {
		releaseCommunity();
		ApiClassLoader.checkAllInstancesAreFreed();
		FileUtils.deleteDirectory(rootFolder());
	}

	protected File rootFolder() throws IOException {
		return _tmpDirectory.createTempDirectory("test");
	}

	private void releaseCommunity() {
		_community = null;
		_a = null;
		_b = null;
		ContainerUtils.stopContainer();
	}
	
	private void createAndConnectParties() {
		
		_a = _community.createParty("Ana Almeida");
		_b = _community.createParty("Bruno Barros");
		
		_a.bidirectionalConnectTo(_b);
	}
}
