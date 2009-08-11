package sneer.bricks.softwaresharing.installer.tests;

import static sneer.foundation.environments.Environments.my;

import java.util.Arrays;

import org.jmock.Expectations;
import org.junit.Test;

import sneer.bricks.pulp.reactive.collections.SetRegister;
import sneer.bricks.pulp.reactive.collections.impl.SetRegisterImpl;
import sneer.bricks.softwaresharing.BrickInfo;
import sneer.bricks.softwaresharing.BrickSpace;
import sneer.bricks.softwaresharing.BrickVersion;
import sneer.bricks.softwaresharing.FileVersion;
import sneer.bricks.softwaresharing.installer.BrickInstaller;
import sneer.foundation.brickness.testsupport.Bind;
import sneer.foundation.brickness.testsupport.BrickTest;

public class BrickInstallerTest extends BrickTest {
	
	@Bind final BrickSpace _brickSpace = mock(BrickSpace.class);
	
	final BrickInstaller _subject = my(BrickInstaller.class);
	
	@Test
	public void test() {
		
		checking(new Expectations() {{
			
			final BrickInfo brick = mock(BrickInfo.class);
			allowing(_brickSpace).availableBricks();
				will(returnValue(newSetRegister(brick).output()));
				
			final BrickVersion version1 = mock("version1", BrickVersion.class);
			final BrickVersion version2 = mock("version2", BrickVersion.class);
			allowing(brick).versions();
				will(returnValue(Arrays.asList(version1, version2)));
				
			allowing(version1).isStagedForExecution();
				will(returnValue(true));
				
			FileVersion interfaceFile = mock("Interface.java", FileVersion.class);
			FileVersion implFile = mock("Impl.java", FileVersion.class);
			
			allowing(version1).files();
				will(returnValue(Arrays.asList(
						interfaceFile,
						implFile)));
				
			allowing(interfaceFile).contents();
				will(returnValue("".getBytes()));
				
			allowing(implFile).contents();
				will(returnValue("".getBytes()));
				
			allowing(version2).isStagedForExecution();
				will(returnValue(false));
		}});
		
		_subject.prepareStagedBricksInstallation();
	}


	private <T> SetRegister<T> newSetRegister(T... elements) {
		final SetRegister<T> result = new SetRegisterImpl<T>();
		result.addAll(Arrays.asList(elements));
		return result;
	}

}
